(ns tools.level-editor.widgets.cellular-automaton
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA FastMath Vector3f]
           [com.jme3.scene.shape Box Quad]) 
  (:require [tools.level-viewer.context :as level-context])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph parser modules color utilities value]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(def cell-shape (Box. 0.2 0.2 0.2))
(defn add-row! [app node generation-count generation-number current-generation]
  (loop [generation (index current-generation)
         position (Vector3f. -8  (- 16 generation-number 0.2) 1.0)]
    (when-let [[[i paint?] & more] (seq generation)]
      (.addLocal position (+ 0.2 0.3) 0 0)
      (attach!* app node (geom :shape cell-shape
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
                               :local-translation position))
      (recur more position))))

(defn compute-generation [rule previous]
    (for [[left old right] (zip (concat [false] previous)
                              previous
                              (concat (rest previous) [false]))]
    (value (invoke* rule core-env [left old right]) core-env)))

(defn compute-generations [rule init]
  (iterate (partial compute-generation rule) init))

(defn build-cellular-automaton [{:keys [x z id direction rule on-error! app]}]
 (let [loc (Vector3f. (* x 16) -16  (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       node (node*  :name id
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    )
       precompute? false
       generations 15
       initial-generation (concat (repeat generations false) [true]
                                  (repeat generations false))
       width (inc (* 2 generations))
       rule (value (code-pog-fn [] "" rule) core-env)
       generation-computer (fn->pog-fn (comp (partial take (inc generations))
                                             (comp index compute-generations)) "" 
                                       ["rule" "init"])
       compute-rows* (do-list-pog-fn 
                        :spatial node
                        :init-wait-time 0.5
                        :queue? false
                        :transformer-id ""
                        ;;:valid-input-type 
                        :queue-init []
                        :interactive? false
                        :on-error! on-error!
                        :app app
                        :on-value!
                          (fn [[index row]]
                            (add-row! app
                                      node
                                      (inc generations)
                                      index
                                      row)))]
   (when precompute?
     (add-row! app node (inc generations) 0 initial-generation)
     (loop [generation 1
            previous initial-generation]
       (when (<= generation generations)
         (let [next-generation (value (compute-generation rule previous) core-env)]
           (add-row! app node (inc generations) generation next-generation)
           (recur (inc generation) next-generation)))
       ))
   (attach-pog-fn! node
      (reify 
        PogFn
        (parameters [_] [{:name "rule" :type (pog-fn-type 3)}])
        (docstring [_] "")
        LazyPogFn
        (lazy-invoke [_ env {{rule :value} "rule"}]
          (invoke* compute-rows* env [on-error! (delay-invoke* generation-computer rule (constantly* initial-generation))]))))
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
   (not old))"] :label "Rule"}]
   :build build-cellular-automaton
   })

