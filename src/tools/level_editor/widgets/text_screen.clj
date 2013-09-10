(ns tools.level-editor.widgets.text-screen
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA FastMath Vector3f]
           [com.jme3.scene.shape Box Quad]) 
  (:require [tools.level-viewer.core :as level-viewer])
  (:use [control assert timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph parser color utilities]
        [seesawx core]))

(defn add-text! [bitmap-text new-text]
  (let [*letters* (atom (seq new-text))]
    (start!
    (control-timer bitmap-text 0.3 true
      (fn []
        (when-let [[letter & letters] @*letters*]
          (.setText bitmap-text (str (.getText bitmap-text)  letter))
          (swap! *letters* (constantly letters))))))))


(defn build-text-screen [{:keys [x z id direction text target-id end-level? app success? parameter docstring success-text error-text]}]
  (try
 (let [loc (Vector3f. (* x 16) -16  (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       *done?* (atom false)
       screen (geom :shape (Quad. 16 16)
                    :material (material :asset-manager app
                                        :color {"Color" ColorRGBA/Black})
                    :local-translation (Vector3f. -8 0 0.01)
                    
                    )
       text* (bitmap-text :font (bitmap-font :asset-manager app
                                             :font-name "Fonts/DejaVuSansMono.fnt")
                          :text text
                          :size 0.5 
                          :box (Rectangle. -8 16 16 16)
                          :alignment :left
                          :color (ColorRGBA. 0.5 1 0 1)
                          :local-translation (Vector3f. 0 0 0.02))
       node (node*
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :children [screen text*])
       ]
   (.updateLogicalState text* 0)
   (if (empty? success?)
     node
     ;node))
     (let [success?* (code-pog-fn [parameter] docstring success?)]
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
                                (start! 
                                  (computation-timer node 5
                                  (fn []
                                    (let [b (invoke* success?* env [message])]
                                      (assert! (implements? Boolean b))
                                      b))
                                  (fn [succeed?]
                                    (if-not succeed?
                                      (when-not (empty? error-text)
                                        (on-error! (Exception. error-text)))
                                      (when-not @*done?*
                                        (swap! *done?* (constantly true))
                                        (when-not (empty? success-text)
                                          (add-text! text* success-text))
                                        (when-not (empty? target-id)
                                          (when-let [target (select app 
                                                                    target-id)]
                                            (invoke* (spatial-pog-fn target )
                                                            [false])))
                                        (when end-level?
                                          (level-viewer/end-level! app))
                                        )

                                          ))
                                  (fn [error]
                                    (on-error! error)))))))
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
               {:id :text  :type [:string :multi-line? true] :label "Text"}
               {:id :success? :type [:string :multi-line? true] :label "Success Test"}
               {:id :parameter :type [:string :text "message"] :label "Parameter"}
               {:id :docstring :type [:string :multi-line? true] :label "Docstring"}
               {:id :success-text :type [:string :multi-line? true] :label "Success Text"}
               {:id :error-text :type [:string :multi-line? true] :label "Error Text"}
               {:id :target-id :type :string :label "Target"}
               {:id :end-level? :type :boolean :label "End Level?"}]
   :prelude `(use '~'tools.level-editor.widgets.text-screen)
   :build (fn [[x z] {:keys [id direction text target-id success? parameter docstring success-text error-text end-level?]}]
            `(do
               (fn [app#]
               (build-text-screen {:x ~x :z ~z :id ~id :direction ~direction :text ~text :target-id ~target-id :app app# :success? ~success? :parameter ~parameter :docstring ~docstring :success-text ~success-text :error-text ~error-text :end-level? ~end-level?}))))})

