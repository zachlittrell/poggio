(ns tools.level-editor.widgets.globule-receiver
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str])
  (:use [control io timer]
        [data coll color object ring-buffer string quaternion]
        [jme-clj animate light geometry material model physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))


(defn build-globule-receiver [{:keys [x y z id direction target-ids  protocol pattern app]}]
 (let [loc (Vector3f. (* x 16) (+ -16 y) (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       hoop (model :asset-manager app
                 :model-name "Models/PowerSource/PowerSource.scene"
                 :name id
                 :local-translation (.subtract loc (.mult dir 
                                                          (Vector3f. 0 0 8)))
                :local-rotation dir
                :controls [control])
       hoop-ball (.getChild (.getChild hoop "Sphere") "Sphere") ]
   (.setMaterial hoop-ball (material :asset-manager app
                                     :color {"Color" ColorRGBA/White}))
   (doto hoop
     (attach-pog-fn!*  
          (keyword->globule-processor-pog-fn :app app
                                             :spatial hoop-ball
                                             :target-ids target-ids
                                             :pattern pattern
                                             :keyword protocol)))))

(def globule-receiver-template
  {:image (image-pad [100 100]
            (.drawString "◎" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :y  :type :integer :label "Height"}
               {:id :target-ids :type [:list 
                                       :type :string] :label "Targets"}
               {:id :protocol :type [:choice
                                     :model [:open-on-pattern
                                             :pass]] :label "Protocol"}
               {:id :pattern :type [:list :type :string] :label "Matches"}]
   :build build-globule-receiver
   })

