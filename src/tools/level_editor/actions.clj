(ns tools.level-editor.actions
  (:use [data quaternion]
        [jme-clj geometry material physics physics-control]
        [poggio.functions core scenegraph color])
  (:import [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.scene.shape Sphere]))


(def globule-shape (Sphere. 32 32 (float 0.4)))
(defn shoot-globule! [app loc dir vel mass color]
  (let [globule-phys (RigidBodyControl. mass)
        globule-phys* (RigidBodyControl. mass)
        globule (geom :shape globule-shape
                      :local-translation loc
                      :local-rotation dir
                      :material (material :asset-manager app
                                          :color {"Color" color})
                      :controls [globule-phys*])]
    (attach! app
             (pog-fn-node 
                :local-translation loc
                :pog-fn (constantly* "RED")
                :local-rotation dir
                :children [globule]
                :controls [globule-phys]))
    (.setLinearVelocity globule-phys*
                        (.mult (quaternion->direction-vector dir)
                               vel))))
