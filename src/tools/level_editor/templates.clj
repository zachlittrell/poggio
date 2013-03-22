(ns tools.level-editor.templates
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f])
  (:use [data quaternion]
        [jme-clj geometry material model physics-control]
        [seesawx core]))

(def function-cannon-template
  {:image (image-pad [100 100]
           (.drawRect 20 30 20 20)
           (.drawLine 20 30 40 50)
           (.drawLine 20 50 40 30)
           (.drawLine 40 50 50 40)
           (.drawLine 40 30 50 40)
                     )
   :questions [{:id :id        :type :string    :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :params    :type :list      :label "Parameter Names"}]
   :build     (fn [[x z] {:keys [id direction]}]
                `(fn [app#] 
                   (let [cannon (model :asset-manager app#
                                       :model-name "Models/Laser/Laser.scene"
                                       :name ~id
                                       :local-translation (Vector3f. (* ~x 16)
                                                                     -16 
                                                                     (* ~z 16))
                                       :local-rotation (angle->quaternion 
                                                         ~direction :y)
                                       :controls [(RigidBodyControl. 0.0)])]



                     cannon)))})
             

                   
