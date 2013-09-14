(ns tools.level-editor.widgets.color-screen
  (:import [java.awt Color]
           [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Quad])
  (:use [control bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate geometry material model physics physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn process-globule! [screen color]
  (let-weave (map? color)
    [color (:value color)]
    []
    (-> (.getMaterial screen)
        (.setColor "Color" (value color {})))))  
  

(defn build-color-screen [{:keys [x z id direction target-id app]}]
 (let [loc (Vector3f. (* x 16) -8 (+ 0.25 (* z 16)))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)
       screen (geom :shape (Box. 8 8 0.25)
                    :material (material :asset-manager app
                                        :color {"Color" ColorRGBA/White})
                    :local-translation (.subtract loc
                                                  (.mult dir
                                                         (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :controls [control])
       ]
   (doto screen
     (attach-pog-fn!*  (fn->pog-fn 
                         (partial process-globule! screen)
                          "color screen"
                          ["color"]
                          (docstr [["color" "A colored globule"]]
                            (str "Activates " target-id 
                                 " once it receives a red,"
                                 " green, and blue globule."))
                                       
                                    )))
    ))

(def color-screen-template
  {:image (image-pad [100 100]
            (.setColor Color/RED)
            (.fillRect 0 0 49 49)
            (.setColor Color/BLUE)
            (.fillRect 50 0 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :target-id :type :string :label "Target"}]
   :prelude `(use '~'tools.level-editor.widgets.color-screen)
   :build (fn [[x z] {:keys [id direction target-id]}]
            `(do
               (fn [app#]
               (build-color-screen {:x ~x  :z ~z :id ~id :direction ~direction :target-id ~target-id :app app#}))))})
