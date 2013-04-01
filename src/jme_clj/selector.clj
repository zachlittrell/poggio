(ns jme-clj.selector
  (:import [com.jme3.scene Node Spatial ])
  (:use [jme-clj node]))

(defn select [node-provider key]
  (.getChild (node node-provider) key))


