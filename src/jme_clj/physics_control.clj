(ns jme-clj.physics-control
  "Functions for creating and handling PhysicsControls"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.bullet.collision.shapes CapsuleCollisionShape]
           [com.jme3.bullet.control CharacterControl]))

(def-opts-constructor character-control
  [:setter]
  {:shape `(CapsuleCollisionShape. 2.0 1 1)
   :step-height 0.05}
  `(CharacterControl. ~'shape ~'step-height)
  {:shape [:no-op]
   :step-height [:no-op]})
