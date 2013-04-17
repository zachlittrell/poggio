(ns jme-clj.transform
  "Generalized functions for performing transformations on
   JME objects."
  (:import [com.jme3.bullet.control CharacterControl]
           [com.jme3.scene Spatial]
           [com.jme3.renderer Camera]))

(defprotocol Warpable
  (location [t] "Returns the current location")
  (warp [t loc] "Moves t to loc."))

(extend-protocol Warpable
  CharacterControl
  (location [control] (.getSpatialTranslation control))
  (warp [control loc] (.warp control loc))
  Camera
  (location [cam] (.getLocation cam))
  (warp [cam loc] (.setLocation cam loc))
  Spatial
  (location [spatial] (.getWorldTranslation spatial))
  (warp [spatial loc] (.setLocalTranslation spatial loc)))

(defprotocol Rotatable
  (rotation [r] "Returns the current rotation")
  (rotate [r dir] "Rotates t to dir"))

(extend-protocol Rotatable
  Camera
  (rotation [cam] (.getRotation cam))
  (rotate [cam dir] (.setRotation cam dir))
  Spatial
  (rotation [spatial] (.getWorldRotation spatial))
  (rotate [spatial dir] (.setLocalRotation spatial dir)))
