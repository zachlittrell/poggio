(ns jme-clj.physics-providers
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState PhysicsSpace]
           [com.jme3.bullet.collision PhysicsCollisionListener]))

(defprotocol PhysicsSpaceProvider
  (physics-space [provider]))

(extend-protocol PhysicsSpaceProvider
  PhysicsSpace
  (physics-space [space] space)
  SimpleApplication
  (physics-space [app] (-> app
                           (.getStateManager)
                           (.getState BulletAppState)
                           (.getPhysicsSpace))))

(defn pause-physics! [app]
  (-> app
     (.getStateManager)
     (.getState BulletAppState)
     (.setEnabled false)))

(defn start-physics! [app]
  (-> app
     (.getStateManager)
     (.getState BulletAppState)
     (.setEnabled true)))

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


