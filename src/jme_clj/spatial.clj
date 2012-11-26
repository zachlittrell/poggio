(ns jme-clj.spatial
  "Functions for Sptial objects"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.scene Geometry]))

(def spatial-constructor-handlers
  {:move [:simple]})

(defmacro def-spatial-constructor [name defaults constructor handlers]
  `(def-opts-constructor ~name
     [:setter]
     ~defaults
     ~constructor
    (merge spatial-constructor-handlers ~handlers)))
     


