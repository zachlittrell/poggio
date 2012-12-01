(ns jme-clj.physics
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]))

(defprotocol PhysicsNode
  (node [node])
  (physics-space [node]))

(extend-type SimpleApplication
  PhysicsNode
  (node [app] (.getRootNode app))
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
