(ns jme-clj.physics
  (:import [com.jme3.bullet.control PhysicsControl])
  (:use [control bindings]
        [jme-clj node physics-providers]))

(defn attach! [physics-node & children]
  "Attaches the children to the node and physics-space provided by 
   physics-node. children can be either a spatial or a vector
   with a spatial and a physics-collision listener."
  (let [physics-space (physics-space physics-node)
        node (node physics-node)]
    (doseq [child children]
      (let-weave (vector? child)
        [[child listener] child]
        []
        [listener] :>> (.addCollisionListener 
                         physics-space
                        (physics-collision-listener listener))
        (.attachChild node child)
        (.addAll physics-space child)))))

(defn detach! [& spatials]
  "Detaches the children from both the scenegraph and the physics-space."
  (doseq [spatial spatials]
    (let [phys-control (.getControl spatial PhysicsControl)]
      (.setEnabled phys-control false))
    (.removeFromParent spatial)))
