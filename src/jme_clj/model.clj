(ns jme-clj.model
  "Functions for handling 3d models."
  (:require [jme-clj.assets :as assets])
  (:use [jme-clj spatial]))

(def-spatial-constructor model
  [:setter]
  {:asset-manager nil
   :model-name    nil}
  `(.loadModel (assets/asset-manager ~'asset-manager) ~'model-name)
  {:asset-manager [:no-op]
   :model-name          [:no-op]})
