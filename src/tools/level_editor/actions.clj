(ns tools.level-editor.actions
  (:use [jme-clj geometry material physics physics-control]
        [poggio.functions color])
  (:import [com.jme3.scene.shape Sphere]))


(def globule-shape (Sphere. 32 32 (float 0.4)))
(defn shoot-globule! [app loc dir vel color]
  (let [globule-phys (rigid-body-control (float 1.0))]
    (attach! app
             (geom :shape globule-shape
                   :material (material :asset-manager app
                                       :texture {"ColorMap"
                                                 color})
                   :local-translation loc
                   :local-rotation dir
                   :controls [globule-phys]))
    (.setLinearVelocity globule-phys (.mult dir vel))))
