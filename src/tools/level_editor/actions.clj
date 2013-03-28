(ns tools.level-editor.actions
  (:use [data quaternion]
        [jme-clj geometry material physics physics-control]
        [poggio.functions color])
  (:import [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.scene.shape Sphere]))


(def globule-shape (Sphere. 32 32 (float 0.4)))
(defn shoot-globule! [app loc dir vel color]
  (let [globule-phys (RigidBodyControl. (float 1.0))]
    (attach! app
             (geom :shape globule-shape
                   :material (material :asset-manager app
                                       :color {"Color" color})
                   :local-translation loc
                   :local-rotation dir
                   :controls [globule-phys]))
    (.setLinearVelocity globule-phys 
                        (.mult (quaternion->direction-vector dir) vel))))
