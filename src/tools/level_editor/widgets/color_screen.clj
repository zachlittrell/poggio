(ns tools.level-editor.widgets.color-screen
  (:import [java.awt Color]
           [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Quad])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate geometry material model physics physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn process-color! [screen {color :value
                              on-error! :on-error!
                              :as input}]
  (try
    (let [color (value color)]
      (assert! (implements? ColorRGBA color))
      (-> (.getMaterial screen)
          (.setColor "Color" color)))
    (catch Exception e
      (when-not (:globule input)
        ;;We don't want to report an error if a globule bumps 
        (on-error! e)))))
  

(defn build-color-screen [{:keys [x z id direction target-id app]}]
 (let [loc (Vector3f. (* x 16) -8 (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       screen (geom :shape (Box. 8 8 0.25)
                    :name id
                    :material (material :asset-manager app
                                        :color {"Color" ColorRGBA/White})
                    :local-translation (.subtract loc
                                                  (.mult dir
                                                         (Vector3f. 0 0 7.75)))
                    :local-rotation dir
                    :controls [control])
       ]
   (doto screen
     (attach-pog-fn!*  (fn->pog-fn 
                         (partial process-color! screen)
                          "color screen"
                          ["color"]
                          (docstr [["color" "A color"]]
                                "Changes its color to color.") 
                                    )))
    ))

(def color-screen-template
  {:image (image-pad [100 100]
            (.setColor Color/RED)
            (.fillRect 0 0 49 49)
            (.setColor Color/BLUE)
            (.fillRect 50 0 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}]
   :build build-color-screen
  ;; :prelude `(use '~'tools.level-editor.widgets.color-screen)
  ;; :build (fn [[x z] {:keys [id direction target-id]}]
  ;;          `(do
  ;;             (fn [app#]
  ;;             (build-color-screen {:x ~x  :z ~z :id ~id :direction ~direction :app app#}))))
   })

