(ns jme-clj.transform
  "Generalized functions for performing transformations on
   JME objects."
  (:import [com.jme3.bullet.control CharacterControl]
           [com.jme3.scene Spatial]
           [com.jme3.renderer Camera]))

(defprotocol Warpable
  (warp [t loc] "Moves t to loc."))

(extend-protocol Warpable
  CharacterControl
  (warp [control loc] (.warp control loc))
  Camera
  (warp [cam loc] (.setLocation cam loc))
  Spatial
  (warp [spatial loc] (.setLocalTranslation spatial loc)))

(defprotocol Rotatable
  (rotate [r dir] "Rotates t to dir"))

(extend-protocol Rotatable
  Camera
  (rotate [cam dir] (.setRotation cam dir))
  Spatial
  (rotate [spatial dir] (.setLocalRotation spatial dir)))
