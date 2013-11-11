(ns tools.level-editor.widgets.cellular-automaton
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA FastMath Vector3f]
           [com.jme3.scene Spatial]
           [com.jme3.scene.shape Box Quad]) 
  (:require [tools.level-viewer.context :as level-context])
  (:use [control assert bindings timer]
        [data coll color either object ring-buffer quaternion zero]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph parser modules color utilities value]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(def cell-shape (Box. 0.2 0.2 0.2))
(defn add-row! [app node generation-count *cells* target-id pair-id generation-number current-generation delay?]
  (let [position (Vector3f. -8 (- 16 generation-number 0.2) 
                            (if delay? -0.25 0))
        row (node*
              :local-translation position)]
  (zero! position)
  (loop [generation (index current-generation)
         position position]
    (if-let [[[i paint?] & more] (seq generation)]
      (do
        (.addLocal position (+ 0.2 0.3) 0 0)
        (let [cell  (geom :shape cell-shape
                          :material (material 
                                      :asset-manager app
                                      :texture 
                                      {"ColorMap" 
                                        (texture 
                                          :asset-manager app
                                          :texture-key 
                                            (if paint?
                                              "Textures/marble2.jpg" 
                                              "Textures/marble1.jpg"))})
                          :local-translation position)]
          (.attachChild row cell)
          (when delay?
            (let [mp (motion-path [position (.add position 0 0 0.25)])]
              (when (and (== generation-number generation-count)
                         (== i 30)
                         (not-empty target-id))
                (.addListener mp
                    (motion-path-listener
                      (fn [mc index]
                          ;;Might want to move this to a thread? For now seems quick enough without.
                        (when (= @*cells* (transformer (spatial-pog-fn-from app pair-id)))
                          (invoke* (spatial-pog-fn-from app target-id)
                                   core-env
                                   [nil false true])
                          )))))
              (follow-path! cell (* i 0.07) 1.75 mp))))
        (recur more position))
      (do
        (swap! *cells* conj current-generation)
        (attach!* app node row))))))

(defn compute-generation [rule previous]
    (for [[left old right] (zip (concat [false] previous)
                              previous
                              (concat (rest previous) [false]))]
     (value (invoke* rule core-env [left old right]) core-env)))

(defn compute-generations [compute-generation rule init]
  (iterate #(invoke* compute-generation core-env [rule %]) init))

(defn build-cellular-automaton 
  [{:keys [x z id direction rule on-error! 
           generations-id row-id rule-id init-id 
           target-id pair-id
           protocol app
           precompute?]}]
 (let [loc (Vector3f. (* x 16) -16  (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       cell-node (node*)
       node (node*  :name id
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :children [cell-node
                               (geom :shape (Quad. 16 16)
                                     :material (material 
                                                 :asset-manager app
                                                 :texture
                                                 {"ColorMap" 
                                                  (texture 
                                                    :asset-manager app
                                                    :texture-key 
                                                    "Textures/tile2.jpg")})
                                     :local-translation
                                       (Vector3f. -8 0 0.01))]
                    )
       generations 15
       [init init-error]
         (if (empty? init-id)
           [(constantly* (concat (repeat generations false) [true]
                                 (repeat generations false)))
            (constantly nil)]
           [(fn->pog-fn (comp transformer
                             (partial spatial-pog-fn-from app init-id))
                       [] "")
            #(on-bad-transform! (spatial-pog-fn-from app init-id))])
       width (inc (* 2 generations))
       [rule rule-error] 
         (if (empty? rule-id)
             (when-not (empty? rule)
                [(code-pog-fn [] "" rule)
                 (constantly nil)])
              [(fn->pog-fn (comp transformer
                                (partial spatial-pog-fn-from app rule-id)))
               #(on-bad-transform! (spatial-pog-fn-from app rule-id))])
       *rule-env* (atom nil)
       [rower rower-error]
         (if (empty? row-id)
           [(fn->pog-fn compute-generation 
                        ""
                        [{:name "rule"
                          :type (pog-fn-type 3)}
                         "init"])
            (constantly nil)]
           [(fn->pog-fn (comp transformer
                              (partial spatial-pog-fn-from app row-id)))
            #(on-bad-transform! (spatial-pog-fn-from app row-id))])
       [generator generator-error]
         (if (empty? generations-id)
           [(fn->pog-fn compute-generations
                        ""
                        [{:name "rower"
                          :type (pog-fn-type 2)}
                         {:name "rule" 
                          :type (pog-fn-type 3)}
                         "init"])
            (constantly nil)]
           [(fn->pog-fn (comp transformer
                              (partial spatial-pog-fn-from app generations-id)))
            #(on-bad-transform! (spatial-pog-fn-from app generations-id))])
       generation-computer 
           (fn->pog-fn (comp (partial take (inc generations))
                             index) 
                       "" 
                       [{:name "generations"
                         :type clojure.lang.Seqable}
                        ])
       *cells* (atom [])
       compute-rows* (do-list-pog-fn 
                        :spatial cell-node
                        :init-wait-time 0.3
                        :queue? false
                        :transformer-id ""
                        :valid-input-type 
                        (predicate-impl
                          "list of booleans"
                          (fn [[index row]]
                            (let [env @*rule-env*
                                  row* (map #(value % env) 
                                            (value row env))]
                              (every? (partial implements? Boolean) row*))))
                        :queue-init []
                        :interactive? false
                        :on-error! on-error!
                        :on-invoke! #(do
                                       (reset! *cells* [])
                                       (.detachAllChildren ^Spatial cell-node))
                        :app app
                        :on-value!
                          (fn [[index row]]
                            (add-row! app
                                      cell-node
                                      generations
                                      *cells*
                                      target-id
                                      pair-id
                                      index
                                      row
                                      true)))]
   (when precompute?
     (let [init (value init core-env)]
     (add-row! app cell-node generations *cells* nil nil 0 init false)
     (loop [generation 1
            previous init]
       (when (<= generation generations)
         (let [next-generation (value (compute-generation rule previous) core-env)]
           (add-row! app cell-node generations *cells* nil nil generation next-generation false)
           (recur (inc generation) next-generation)))
       )))
   (attach-pog-fn! node
      (reify 
        Transform
        (transformer [_]
          @*cells*)
        PogFn
        (parameters [_] ["argument"])
        (docstring [_] "")
        LazyPogFn
        (lazy-invoke [_ env {{arg :value} "argument"}]
          (reset! *rule-env* env)
           (try*
             (let [[rule init rower generator]
                     (case protocol
                       :all-transform
                         [rule init rower generator]
                       :rule
                         [arg init rower generator]
                       :init
                         [rule arg rower generator]
                       :rower
                         [rule init arg generator]
                       :generator
                         [rule init rower arg])]
               (invoke* compute-rows* env 
                  [on-error!
                   (delay-invoke* generation-computer 
                      (delay-invoke* generator rower rule init))]))
             e
             (do
               (on-error!))))))
   node
    ))

(def cellular-automaton-template
  {:image (image-pad [100 100]
            (.drawString "‱" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}

               {:id :rule :type [:string :multi-line? true
                                 :init 
"(function [left old right] 
   (not old))"] :label "Rule"}
               {:id :rule-id :type :string :label  "Rule Transform Id"}
               {:id :row-id :type :string :label "Row Transform Id"}
               {:id :generations-id :type :string :label "Generations Transform Id"}
               {:id :init-id :type :string :label "Init Id"}
               {:id :precompute? :type :boolean :label "Precompute?"}
               {:id :protocol :type [:choice
                                     :model [:all-transform
                                             :rule
                                             :init
                                             :rower
                                             :generator]] 
                :label "Protocol"}
               {:id :target-id :type :string :label "Target"}
               {:id :pair-id :type :string :label "Pair"}]
   :build build-cellular-automaton
   })
