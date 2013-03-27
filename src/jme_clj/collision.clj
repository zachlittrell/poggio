(ns jme-clj.collision
  "Methods for handling collisions in JME."
  (:import [com.jme3.bullet.collision PhysicsCollisionListener]
           [com.jme3.collision Collidable CollisionResults]
           [com.jme3.math Ray]
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
  ([^Camera camera ^Collidable collidable] (closest-collision collidable (Ray. (.getLocation camera) (.getDirection camera)))))

(defprotocol PhysicsCollisionListenerProvider
  (physics-collision-listener [this]))

(extend-protocol PhysicsCollisionListenerProvider
  PhysicsCollisionListener
  (physics-collision-listener [this] this)
  clojure.lang.IFn
  (physics-collision-listener [f]
    (reify PhysicsCollisionListener
      (collision [_ e]
        (f e)))))

(defmacro object-collision-listener [obj [this obj1 obj2 e] & body]
  "Returns a PhysicsCollisionListener which only executes body when
   one of the collided nodes are obj. obj1 is obj and obj2 is the node
   that was collided with. e is the CollisionEvent."
  `(let [obj# ~obj]
     (reify PhysicsCollisionListener
       (collision [~this ~e]
          (when-let [[~obj1 ~obj2] (condp identical? obj#
                                       (.getNodeA ~e) [(.getNodeA ~e)
                                                         (.getNodeB ~e)]
                                       (.getNodeB ~e) [(.getNodeB ~e)
                                                        (.getNodeA ~e)]
                                       nil)]
            ~@body)))))

(defmacro objects-collision-listener [obj1 obj2 [this obj1* obj2* e] & body]
  "Returns a PhysicsCollisionListener which only executes body when
   obj1 collides with obj2. obj1* and obj2* are mapped accordingly. 
   e is the CollisionEvent."
  `(let [obj2# ~obj2]
     (object-collision-listener ~obj1 [~this ~obj1* ~obj2* ~e]
       (if (identical? obj2# ~obj2*)
         ~@body))))

 
(defmacro on-collision! [physics-space bindings & body]
  "Adds a PhysicsCollisionListener to the physics-space
   which uses bindings for its parameters and body for the
   collision function body."
  `(.addCollisionListener physics-space
      (reify PhysicsCollisionListener
        (collision ~bindings ~@body))))
