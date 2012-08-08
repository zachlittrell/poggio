(ns jme-clj.geometry
  (:use [functions.utilities :only [no-op]]
        [control.defhelpers :only [def-opts-constructor]])
  (:import [com.jme3.material Material] 
           [com.jme3.math Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Sphere]))

(def-opts-constructor geom
  {:label "geom-generated geometry"
   :shape (Sphere. 16 16 1)}
  (Geometry. label shape)
  {:label       no-op
   :shape       no-op
   :material    (fn [^Geometry geom ^Material mat]
                 (.setMaterial geom mat))
   :rotation    (fn [^Geometry geom ^Vector3f rot]
                  (.setLocalRotation geom rot))
   :translation (fn [^Geometry geom ^Vector3f loc]
                  (.setLocalTranslation geom loc))})
