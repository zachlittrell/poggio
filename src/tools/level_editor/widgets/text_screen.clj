(ns tools.level-editor.widgets.text-screen
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA FastMath Vector3f]
           [com.jme3.scene.shape Box Quad]) 
  (:require [clojure.string :as str]
           [tools.level-viewer.context :as level-context])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph parser modules list number color utilities value]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(defn add-text! [bitmap-text new-text app end-level?]
  (let [*letters* (atom (seq new-text))]
    (start!
    (control-timer bitmap-text 0.3 true
      (fn []
        (if-let [[letter & letters] @*letters*]
          (do
            (.setText bitmap-text (str (.getText bitmap-text)  letter))
            (swap! *letters* (constantly letters))
            true)
          (when end-level?
            (level-context/end-level! app true))))))))


(defn build-text-screen [{:keys [x z id direction text target-ids end-level? app  transform success? parameter docstring success-text error-text text-color font-size distance on-error! protocol]
                         :or
                         {distance 40}}]
  (try
 (let [loc (Vector3f. (* x 16) -16  (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       screen (geom :shape (Quad. 16 16)
                    :material (material :asset-manager app
                                        :color {"Color" ColorRGBA/Black})
                    :local-translation (Vector3f. -8 0 0.01)
                    
                    )
       text* (bitmap-text :font (bitmap-font :asset-manager app
                                             :font-name "Fonts/DejaVuSansMono.fnt")
                          :text text
                          :size font-size
                          :box (Rectangle. -8 16 16 16)
                          :alignment :left
                          :color (ColorRGBA. (/ (:red text-color) 255.0) 
                                             (/ (:green text-color) 255.0)
                                             (/ (:blue text-color) 255.0)
                                             1.0)
                          :local-translation (Vector3f. 0 0 0.02))
       node (node*  :name id
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :children [screen text*])
       [protocol transform] (cond (= protocol :pass) [protocol ""]
                                  (= protocol :pass-with) [:pass transform]
                                  :else [protocol transform])
       ]
   (.updateLogicalState text* 0)
   (case protocol
     :none node
     :pass 
          (let [t (if (empty? transform) 
                    [] 
                    [(code-pog-fn [] "" transform)])]
          (doto node
           (attach-pog-fn!* 
            (reify PogFn
             (parameters [_] [{:name "player" :type Warpable}
                              "on-error!" parameter])
             (docstring [_] docstring)
            LazyPogFn
            (lazy-invoke [_ env {player "player" on-error! "on-error!"
                                 message parameter}]
              (when-close-enough on-error! player node distance
              (start!
                (computation-timer node 5
                  (fn []
                    (invoke* message env t))
                  (fn [result]
                    (doseq [target-id target-ids]
                      (when-let [t (select app target-id)]
                        (let [pog-fn (spatial-pog-fn t)]
                          (if (== (count (parameters pog-fn)) 1)
                           (invoke* pog-fn {} 
                                    [{:on-error! on-error!
                                      :value result}])
                            (invoke* pog-fn {} [nil result]))))))
                  (fn [error]
                    (on-error! error))))))))))

     :hold (let [default-transform {:transform (code-pog-fn [] "" transform)
                                    :env core-env
                                    :*cache* (atom nil)}
                 *transformer* (atom  default-transform)]
             (.setText text* (str text "\n\nUSING DEFAULT FUNCTION:\n\n"
                                 (source-code (:transform default-transform))))
             (attach-pog-fn!* node
              (reify PogFn
                    (parameters [_] [{:name "player" :type Warpable}
                                    "on-error!" parameter])
                    (docstring [_] docstring)
                Transform
                (transformer [_]
                   (let [{:keys [transform on-error! env *cache*]} @*transformer*]
                    (if-let [transform @*cache*]
                      transform
                      (let [transform (reset! *cache* (value transform env))]
                        transform
                        ))
                    ))

                (transform [_ obj]
                  (let [{:keys [transform on-error! env *cache*]} @*transformer*]
                    (if-let [transform @*cache*]
                      (invoke* transform env [obj])
                      (let [transform (reset! *cache* (value transform env))]
                        (invoke* transform env [obj])
                        ))
                    ))
                (on-bad-transform! [_]
                    (.setText text* (str text
                                         "\n\nUSING DEFAULT FUNCTION:\n\n"
                                         (source-code (:transform default-transform))))
                    (reset! *transformer* (assoc default-transform
                                                 :*cache* (atom nil))))
                LazyPogFn
                (lazy-invoke [_ env {player "player"
                                     on-error! "on-error!"
                                     message parameter}]
                  (when-close-enough on-error! player node distance
                  (.setText text* (str text 
                                       "\n\nUSING USER SUBMITTED FUNCTION:\n\n"
                                       (source-code message)))
                  (reset! *transformer* {:transform message
                                         :on-error! on-error!
                                         :*cache* (atom nil)
                                         :env env})
                             ))))
             node)
     :cmd-prompt (let [printer (do-list-pog-fn
                                 :spatial node
                                 :init-wait-time 0.1
                                 :on-value! #(.setText text*
                                              (str (.getText text*) 
                                                   %))
                                 :on-invoke! #(.setText text*
                                                  (str text "\n\n"))
                                 :on-error! on-error!
                                 :interactive? false
                                 :app app)
                       str** (fn->pog-fn str* ""
                                  ["o"])] 
                   (attach-pog-fn!* node
                      (seq->pog-fn ""
                        [{:name "player"
                          :type Warpable}
                         "on-error!"
                          parameter]
                        (docstr [[parameter "a value"]]
                                (str "prints out " parameter))
                         (list printer (var* "on-error!")
                          (list str** (var* parameter)))))
                   node)
     :menus (let [menus (read-string transform)
                  *state* (atom nil)
                  run-state* (fn->pog-fn 
                               (fn [state]
                                 (assert! (integral? state))
                                 (let [current-state @*state*
                                       {:keys [states]} (menus current-state)]
                                   (assert! (in-bounds? state 0 (dec (count states))))
                                   (:state (states (long state)))))
                               ""
                               [{:name "state" :type Number}])
                    
                  handle-state! (fn [state]
                                  (let [{:keys [states label]} (menus state)]
                                   (reset! *state* state)
                                   (.setText text* 
                                      (str text 
                                           label "\n"
                                           (str/join "\n"
                                             (for [[i label]
                                                   (index (map :label states))]
                                               (str i ". " label)))))))

                  waiter (do-list-pog-fn
                           :spatial node
                           :init-wait-time 0.1
                           :on-value! handle-state!
                           :on-error! on-error!
                           :interactive? false
                           :app app)]
              (handle-state! (:start menus))
              (attach-pog-fn!* node
                (seq->pog-fn ""
                  [{:name "player" :type Warpable}
                   "on-error!"
                   parameter]
                  (docstr [[parameter "an integer"]]
                          "selects the given option.")
                  (list waiter (var* "on-error!")
                    (list single* (list run-state* (var* parameter))))))
              node)
     :open (let [success?* (code-pog-fn [parameter] docstring success?)
                *computation* (atom nil)
                 *done?* (atom false)]
            (attach-pog-fn!* node (reify 
                                   PogFn
                                   (parameters [_]
                                     [{:name "player" :type Warpable}
                                      "on-error!" 
                                      parameter])
                                   (docstring [_] docstring)
                                   LazyPogFn
                                   (lazy-invoke [_ env {player "player"
                                                        on-error! "on-error!"
                                                        message parameter}]
                                    (when-close-enough on-error! player node distance
                                     (when-not @*done?*
                                       (when-let [timer @*computation*]
                                         (stop! timer))
                                       (start! (swap! *computation*
                                         (constantly
                                          (computation-timer node 5
                                         (fn []
                                           (let [b (invoke* success?* env [message])]
                                             (assert! (implements? Boolean b))
                                             b))
                                         (fn [succeed?]
                                           (if-not succeed?
                                             (when-not (empty? error-text)
                                               (on-error! (Exception. error-text)))
                                             (do
                                               (swap! *done?* (constantly true))
                                               (when-not (empty? success-text)
                                                 (add-text! text* success-text app end-level?))
                                               (doseq [target-id target-ids]
                                                 (when-let [target (select app 
                                                                           target-id)]
                                                   (invoke* (spatial-pog-fn target)
                                                                   [nil nil true])))
                                               )

                                               ))
                                       (fn [error]
                                         (on-error! error)))))))))))
             node 
            )))
   (catch Exception e
     (.printStackTrace e)))
    )

(def text-screen-template
  {:image (image-pad [100 100]
            (.drawString "¶" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :text-color :type [:color
                                       :init {:red 127 :green 255 :blue 0}]
                                :label "Color"}
               {:id :font-size :type [:decimal :init 0.6] :label "Font Size"}
               {:id :text  :type [:string 
                                  :text "POGGIO INSTITUTE\n================\n"
                                  :multi-line? true] :label "Text"}
               {:id :protocol :type [:choice
                                     :model [:none :open :pass :pass-with
                                             :hold
                                             :cmd-prompt
                                             :menus]] :label "Protocol"}
               {:id :transform :type [:string :text "(function [x] x)" :multi-line? true] :label "Transform"}
               {:id :success? :type [:string :multi-line? true] :label "Success Test"}
               {:id :parameter :type [:string :text "message"] :label "Parameter"}
               {:id :docstring :type [:string :multi-line? true] :label "Docstring"}
               {:id :success-text :type [:string :multi-line? true] :label "Success Text"}
               {:id :error-text :type [:string :multi-line? true] :label "Error Text"}
               {:id :target-ids :type [:list :type :string] :label "Target"}
               {:id :distance :type [:integer
                                     :init 40] :label "Distance"}
               {:id :end-level? :type :boolean :label "End Level?"}]
   :build build-text-screen
   })

