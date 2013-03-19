(ns tools.level-editor.templates
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.math Vector3f])
  (:use [data quaternion]
        [jme-clj geometry model]
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
               {:id :direction :type :direction :label "Direction"}]
   :build     (fn [[x z] {:keys [id direction]}]
                `(fn [asset-manager#] 
                   (let [model# (model :asset-manager asset-manager#
                                       :name "cannon.scene")]
                   (geom :shape model#
                         :label ~id
                         :local-translation (Vector3f. ~x -2 ~z)
                         :local-rotation (angle->quaternion ~direction :y)
                         :controls [(RigidBodyControl.
                                      (CollisionShapeFactory/createMeshShape
                                        model#) 0)]))))})
             

                   
