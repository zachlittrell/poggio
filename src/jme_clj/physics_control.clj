(ns jme-clj.physics-control
  "Functions for creating and handling PhysicsControls"
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.bullet.collision.shapes CapsuleCollisionShape]
           [com.jme3.bullet.control CharacterControl RigidBodyControl]
           [com.jme3.bullet.util CollisionShapeFactory]))

(def-opts-constructor character-control
  [:setter]
  {:shape `(CapsuleCollisionShape. 2 2 1)
   :step-height 0.05}
  `(CharacterControl. ~'shape ~'step-height)
  {:shape [:no-op]
   :step-height [:no-op]})

(def-opts-constructor rigid-body-control
  [:setter]
  {:shape nil
   :mass  0}
  `(RigidBodyControl. ~'shape ~'mass)
  {:shape [:no-op]
   :mass  [:no-op]})

(defn static-spatial-control [spatial]
  (rigid-body-control :shape (CollisionShapeFactory/createMeshShape spatial)
                      :mass 0))
