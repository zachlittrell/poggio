(ns tools.level-editor.widgets.text-screen
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
        [seesawx core]))

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
            (level-context/end-level! app))))))))


(defn build-text-screen [{:keys [x z id direction text target-id end-level? app transform-param transform success? parameter docstring success-text error-text font-size protocol]}]
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
                          :color (ColorRGBA. 0.5 1 0 1)
                          :local-translation (Vector3f. 0 0 0.02))
       node (node*  :name id
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :children [screen text*])
       ]
   (.updateLogicalState text* 0)
   (case protocol
     :none node
     :pass (doto node
           (attach-pog-fn!* 
            (reify PogFn
             (parameters [_] [{:name "player" :type Warpable}
                              "on-error!" parameter])
             (docstring [_] docstring)
            LazyPogFn
            (lazy-invoke [_ env {player "player" on-error! "on-error!"
                                 message parameter}]
              (start!
                (computation-timer node 5
                  (fn []
                    (invoke* message env []))
                  (fn [result]
                    (when-let [t (select app target-id)]
                      (invoke* (spatial-pog-fn t) {} [result])))
                  (fn [error]
                    (on-error! error))))))))

     :hold (let [default-transform {:transform (code-pog-fn [transform-param] "" transform)
                                    :env core-env}
                 *transformer* (atom  default-transform)]
             (.setText text* (str text "\n\nUSING DEFAULT FUNCTION."))
             (attach-pog-fn!* node
              (reify PogFn
                    (parameters [_] [{:name "player" :type Warpable}
                                    "on-error!" parameter])
                    (docstring [_] docstring)
                Transform
                (transform [_ obj]
                  (let [{:keys [transform on-error! env]} @*transformer*]
                      (invoke* transform env [obj])
                    ))
                (on-bad-transform! [_]
                    (.setText text* (str text
                                         "\n\nUSING DEFAULT FUNCTION."))
                    (reset! *transformer* default-transform))
                LazyPogFn
                (lazy-invoke [_ env {player "player"
                                     on-error! "on-error!"
                                     message parameter}]
                  (.setText text* (str text 
                                       "\n\nUSING USER SUBMITTED FUNCTION."))
                  (reset! *transformer* {:transform message
                                         :on-error! on-error!
                                         :env env})
                             )))
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
                                               (when-not (empty? target-id)
                                                 (when-let [target (select app 
                                                                           target-id)]
                                                   (invoke* (spatial-pog-fn target )
                                                                   [false])))
                                               )

                                               ))
                                       (fn [error]
                                         (on-error! error))))))))))
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
               {:id :font-size :type [:decimal :init 0.6] :label "Font Size"}
               {:id :text  :type [:string 
                                  :text "POGGIO INSTITUTE\n================\n"
                                  :multi-line? true] :label "Text"}
               {:id :protocol :type [:choice
                                     :model [:none :open :pass :hold]] :label "Protocol"}
               {:id :transform-param :type [:string :text "x"] :label "Transform Param"}
               {:id :transform :type [:string :text "x" :multi-line? true] :label "Transform"}
               {:id :success? :type [:string :multi-line? true] :label "Success Test"}
               {:id :parameter :type [:string :text "message"] :label "Parameter"}
               {:id :docstring :type [:string :multi-line? true] :label "Docstring"}
               {:id :success-text :type [:string :multi-line? true] :label "Success Text"}
               {:id :error-text :type [:string :multi-line? true] :label "Error Text"}
               {:id :target-id :type :string :label "Target"}
               {:id :end-level? :type :boolean :label "End Level?"}]
   :build build-text-screen
  ;; :prelude `(use '~'tools.level-editor.widgets.text-screen)
  ;; :build (fn [[x z] {:keys [id direction text target-id font-size transform-param transform success? parameter docstring success-text error-text end-level? protocol]}]
  ;;          `(do
  ;;             (fn [app#]
  ;;             (build-text-screen {:x ~x :z ~z :id ~id :direction ~direction :text ~text :target-id ~target-id :app app# :transform-param ~transform-param :transform ~transform :success? ~success? :font-size ~font-size :parameter ~parameter :docstring ~docstring :success-text ~success-text :error-text ~error-text :end-level? ~end-level? :protocol ~protocol}))))
   })

