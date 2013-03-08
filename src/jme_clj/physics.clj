(ns jme-clj.physics
  (:use [jme-clj collision node])
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
  "Attaches the children to the node and physics-space provided by 
   physics-node. children can be either a spatial or a vector
   with a spatial and a physics-collision listener."
  (let [physics-space (physics-space physics-node)
        node (node physics-node)]
    (doseq [child-vec children]
      (let [[child listener] (if (vector? child-vec) 
                                    child-vec 
                                    [child-vec])]
        (when listener
          (.addCollisionListener physics-space
                                 (physics-collision-listener listener)))
        (.attachChild node child)
        (.add physics-space child)))))
