(ns tools.level-editor.widgets.ball-stereo
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math FastMath ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str]
            [tools.level-editor.widgets.function-cannon :as cannon])
  (:use [control io timer]
        [data coll color notes object ring-buffer string quaternion]
        [jme-clj animate light geometry material model node physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(def note-number->color
  [(ColorRGBA. 0.15 1.0 0 1.0)
   (ColorRGBA. 0 1.0 0.94 1)
   (ColorRGBA. 0 0.47 1.0 1)
   (ColorRGBA. 5 0 1.0 1)
   (ColorRGBA. 0.27 0 0.92 1.0)
   (ColorRGBA. 0.38 0 0.69 1.0)
   (ColorRGBA. 0.68 0 0 1.0)
   (ColorRGBA. 1.0 0 0 1.0)
   ColorRGBA/Magenta
   (ColorRGBA. 1.0 0.4 0 1.0)
   ColorRGBA/Yellow
   (ColorRGBA. 0.6 1.0 0 1.0)])
   
(defn shoot-globule! [app on-error! loc dir note]
  (println "HELLLLLOOO"
           (pitch note) (mod (pitch note) 12))
  (let [c (if (rest? note) ColorRGBA/White (note-number->color
                                             (mod (pitch note) 12)))
        d (duration note)]
    (cannon/shoot-globule! app on-error! nil loc dir
                           (float (+ 20.0 (* 30.0 (- 1.1 (duration note)))))
                           0.5
                           c)))
                          


(defn build-ball-stereo [{:keys [x z id target-id  protocol pattern app on-error!]}]
 (let [new-hoop (fn [loc dir id]
                  (let [hoop (model 
                               :asset-manager app
                               :model-name "Models/PowerSource/PowerSource.scene"
                               :name id
                               :local-translation loc
                               :local-rotation dir
                               )]
                    (.scale hoop 1.5 1.5 1.5)
;                    (.addControl hoop (RigidBodyControl. 0.0))
                    [hoop (.getChild (.getChild hoop "Sphere") "Sphere")]))
       [hoop hoop-ball] (new-hoop (Vector3f. 0 -16 2.5)
                                  (angle->quaternion FastMath/PI :x)
                                  id)
       [hoop2 hoop-ball2] (new-hoop (Vector3f. 0 0 -2.5)
                                    (angle->quaternion FastMath/TWO_PI
                                                       :x)
                                    "")
       node (node* :local-translation (Vector3f. (* x 16) 0 (* z 16))
                   :children [hoop hoop2])]
   (.addControl hoop (RigidBodyControl. 0.0))
   (.addControl hoop2 (RigidBodyControl. 0.0))
   (.setMaterial hoop-ball2 (material :asset-manager app
                                      :color {"Color" ColorRGBA/White}))
   (attach-pog-fn!* hoop
      (do-list-pog-fn :spatial hoop
                      :init-wait-time 0.5
                      :on-value! (partial shoot-globule! app
                                                       on-error!
                                                       (.add (.getWorldTranslation
                                                               hoop-ball) 
                                                             0 3 0)
                                                       (angle->quaternion
                                                         FastMath/PI
                                                         :x))
                      :queue? true
                      :transformer-id ""
                      :queue-init []
                      :param "globules"
                      :handle-continuation? false
                      :app app))
   (attach-pog-fn!* hoop2
      (keyword->globule-processor-pog-fn :app app
                                         :spatial hoop-ball2
                                         :target-id target-id
                                         :pattern pattern
                                         :keyword protocol))
         node))

(def ball-stereo-template
  {:image (image-pad [100 100]
            (.drawString "!" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :target-id :type :string :label "Target"}
               {:id :protocol :type [:choice
                                     :model [:open-on-pattern
                                             :pass]]
                              :label "Protocol"}]
   :build build-ball-stereo
   })

