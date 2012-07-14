(ns jme-clj.geometry
  (:use [functions.utilities :only [no-op]])
  (:import [com.jme3.material Material] 
           [com.jme3.math Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Sphere]))

(def ^{:doc "The default handlers of options given to geom"}
  geom-opt-handlers
  {:label       no-op
   :shape       no-op
   :material    (fn [^Geometry geom ^Material mat]
                  (.setMaterial geom mat))
   :rotation    (fn [^Geometry geom ^Vector3f rot]
                  (.setLocalRotation geom rot))
   :translation (fn [^Geometry geom ^Vector3f loc]
                  (.setLocalTranslation geom loc))})

(defn geom [& opts]
  "Returns a new Geometry object using the options passed to
   opts.
   Supported options are:
   :label  The label of the geometry
   :shape  The shape of the geometry
   :material  The initial material of the geometry
   :rotation  The initial rotation of the geometry
   :translation  The initial translation of the geometry"
  (let [{label :label
         shape :shape
         :as opts-map
         :or
         {label "geom-generated geometry"
          shape (Sphere. 16 16 1)}} opts
         geom ^Geometry (Geometry. label shape)]
    (doseq [[key val] opts-map]
      (if-let [handle (geom-opt-handlers key)]
        (handle geom val)
        (binding [*out* *err*] (println key " is not a valid option."))))
     geom))
    
