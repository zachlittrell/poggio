(ns jme-clj.spatial
  "Functions for Sptial objects"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.scene Geometry]))

(def spatial-constructor-handlers
  "The default handlers for Spatial constructors"
  {:move [:simple]
   :controls [:simple
              :replace [#"^(.*)$","add-control"]]})

(defmacro def-spatial-constructor [name default defaults constructor handlers]
  "Creates a constructor, specifically for Spatial subclasses,
   with defaults provided."
  `(def-opts-constructor ~name
     ~default
     ~defaults
     ~constructor
    (merge spatial-constructor-handlers ~handlers)))
     


