(ns jme-clj.physics
  (:use [jme-clj node])
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet PhysicsSpace]
           [com.jme3.bullet BulletAppState]))

(defprotocol PhysicsSpaceProvider
  (physics-space [provider]))

(extend PhysicsSpace
  PhysicsSpaceProvider
  {:physics-space identity})

(extend-type SimpleApplication
  PhysicsSpaceProvider
  (physics-space [app] (-> app
                           (.getStateManager)
                           (.getState BulletAppState)
                           (.getPhysicsSpace))))

(defn attach! [physics-node & children]
  (let [physics-space (physics-space physics-node)
        node (node physics-node)]
    (doseq [child children]
      (.attachChild node child)
      (.add physics-space child))))
