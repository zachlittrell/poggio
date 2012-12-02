(ns jme-clj.collision
  "Methods for handling collisions in JME."
  (:import [com.jme3.bullet.collision PhysicsCollisionListener]
           [com.jme3.collision Collidable CollisionResults]
           [com.jme3.math Ray]
           [com.jme3.renderer Camera]))

(defn closest-collision 
  "Returns the closest collision between collidable and other,
   or nil if there is none."
  ([^Collidable collidable ^Collidable other]
    (let [results (CollisionResults.)]
      (.collideWith other collidable results)
      (when (> (.size results) 0)
        (.getClosestCollision results)))))

(defn closest-collision-from-camera
  "Returns the closest collision between collidable and camera,
   or nil if there is none."
  ([^Camera camera ^Collidable collidable]
    (closest-collision collidable (Ray. (.getLocation camera)
                                        (.getDirection camera)))))
 
(defmacro on-collision! [physics-space bindings & body]
  "Adds a PhysicsCollisionListener to the physics-space
   which uses bindings for its parameters and body for the
   collision function body."
  `(.addCollisionListener physics-space
      (reify PhysicsCollisionListener
        (collision ~bindings ~@body))))
