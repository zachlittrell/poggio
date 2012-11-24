(ns jme-clj.geometry
  "Functions for creating Geometry objects."
  (:use [functions.utilities :only [no-op]]
        [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.material Material] 
           [com.jme3.math Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Sphere]))

(def-opts-constructor geom
  [:setter]
  {:label "geom-generated geometry"
   :shape `(Sphere. 16 16 1)}
  `(Geometry. ~'label ~'shape)
  {:label [:no-op]
   :shape [:no-op]
   :move  [:simple]})

