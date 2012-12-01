(ns jme-clj.light
  "Functions for handling light in JME"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.light AmbientLight DirectionalLight]))


(defmacro def-light-constructor [name default defaults constructor handlers]
  "Creates a constructor for a Light subclass with pre-provided handlers."
  `(def-opts-constructor ~name
                         ~default
                         ~defaults
                         ~constructor
                         ~handlers ))

(def-light-constructor light
  [:setter]
  {}
  `(Light.)
  {})

(def-light-constructor ambient-light
  [:setter]
  {}
  `(AmbientLight.)
  {})

(def-light-constructor directional-light
  [:setter]
  {}
  `(DirectionalLight.)
  {})

