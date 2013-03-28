(ns tools.level-editor.templates
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f])
  (:use [data quaternion]
        [jme-clj geometry material model physics-control]
        [poggio.functions core scenegraph color]
        [seesawx core]
        [tools.level-editor actions]))

(defn build-function-cannon [x z id direction app]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       cannon (model :asset-manager app
                 :local-translation loc
                :local-rotation dir
                :controls [(RigidBodyControl. 0.0)]
                    :model-name "Models/Laser/Laser.scene")]
   (pog-fn-node :name id
                :local-translation loc
                :local-rotation dir
                :controls [(RigidBodyControl. 0.0)]
                :pog-fn (fn->pog-fn (partial shoot-globule!
                                     app
                                     (.add loc 
                                           (.mult dir (Vector3f. 0 4 2.1)))
                                     dir
                                     (float 25)
                                     (value red*))
                                     "" 
                                     [])
                :children [cannon])))
 
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
   :prelude `(use '~'tools.level-editor.templates)
   :build     (fn [[x z] {:keys [id direction]}]
                `(do
                   (fn [app#] 
                   (build-function-cannon ~x ~z ~id ~direction app#))))})

            

                   
