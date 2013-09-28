(ns jme-clj.spatial
  "Functions for Sptial objects"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.scene Geometry Spatial]))

(def spatial-constructor-handlers
  "The default handlers for Spatial constructors"
  {:move [:simple]
   :controls [:do-seq
              :replace [#"^(.*)$","add-control"]]})

(defmacro def-spatial-constructor
  "Creates a constructor, specifically for Spatial subclasses,
   with defaults provided."
  [name default defaults constructor handlers]
  `(def-opts-constructor ~name
     ~default
     ~defaults
     ~constructor
    (merge spatial-constructor-handlers ~handlers)))

(defrecord UserData [data]
  com.jme3.export.Savable)

(defn set-user-data! [^Spatial spatial key obj]
  (.setUserData  spatial key (UserData. obj)))

(defn has-user-data? [^Spatial spatial key]
  (not (.getUserData spatial key)))

(defn get-user-data! [^Spatial spatial key]
  (when-let [val (.getUserData spatial key)]
    (:data val)))

(defmacro do-dfs [[sym tree] & body]
  `(.depthFirstTraversal ~tree
      (reify com.jme3.scene.SceneGraphVisitor
        (visit [visitor# ~sym]
          ~@body))))
