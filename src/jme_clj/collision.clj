(ns jme-clj.collision
  "Methods for handling collisions in JME."
  (:require [jme-clj.physics-providers :as physics])
  (:use [data coll]
        [jme-clj physics-providers])
  (:import [com.jme3.bullet.collision PhysicsCollisionListener]
           [com.jme3.collision Collidable CollisionResults]
           [com.jme3.math Ray Vector2f]
           [com.jme3.renderer Camera]))

(defn collisions  [^Collidable collidable ^Collidable other]
  "Returns the collisions between collidable and other"
  [^Collidable collidable ^Collidable other]
  (let [results (CollisionResults.)]
    (.collideWith collidable other results)
    results))

(defn closest-collision 
  "Returns the closest collision between collidable and other,
   or nil if there is none."
  ([^Collidable collidable ^Collidable other]
    (let [results (collisions collidable other)]
      (when (> (.size results) 0)
        (.getClosestCollision results)))))

(defn closest-collision-from-camera
  "Returns the closest collision between collidable and camera,
   or nil if there is none."
  ([^Camera camera ^Collidable collidable] 
   (closest-collision collidable (Ray. (.getLocation camera) 
                                       (.getDirection camera)))))

(defn closest-collision-from-point 
  "Returns the closest collision between collidable and a point,
   or nil if there is none. Uses camera to calculate coordinates."
  [^Camera cam ^Vector2f point ^Collidable collidable]
  (let [point3d (-> (.getWorldCoordinates cam (Vector2f. (.x point)
                                                         (.y point))
                                              (float 0)))]
  (closest-collision collidable 
                     (Ray. point3d
                           (-> (.getWorldCoordinates cam
                                                     (Vector2f. (.x point)
                                                                (.y point))
                                                     (float 1))
                               (.subtractLocal point3d)
                               (.normalizeLocal))))))
                                                     


(defmacro pred-collision-listener
  "Returns a Physics CollisionListener which only executes body when
   the colliding objects match preds in some permutation."
  [preds [this obj1 obj2 e] & body]
  `(let [preds# ~preds]
     (reify PhysicsCollisionListener
       (collision[~this ~e]
          (when-let [[~obj1 ~obj2] (perm-some preds# [(.getNodeA ~e)
                                                      (.getNodeB ~e)])]
            ~@body)))))

(defmacro on-collision!
  "Adds a PhysicsCollisionListener to the physics-space
   which uses bindings for its parameters and body for the
   collision function body."
  [physics-space bindings & body]
  `(.addCollisionListener (physics/physics-space ~physics-space)
      (reify PhysicsCollisionListener
        (collision ~bindings ~@body))))

(defmacro on-pred-collision!
  "Adds a predicated PhysicsCollisionListener to the physics-space.
   See pred-collision-listener for details."
  [physics-space preds [this obj1 obj2 e :as bindings]  & body]
  `(.addCollisionListener (physics/physics-space ~physics-space)
      (pred-collision-listener ~preds ~bindings ~@body)))


