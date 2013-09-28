(ns jme-clj.physics
  (:import [com.jme3.bullet.control PhysicsControl])
  (:use [control bindings]
        [jme-clj node physics-providers]))

(defn attach!*
  "Attaches the children to the node and physics-space provided by 
   physics-node. children can be either a spatial or a vector
   with a spatial and keyword-arguments. Currently only supported
   argument is :kinematic?."
 ([physics node* & children]
  (let [physics-space (physics-space physics)
        node (node node*)]
    (doseq [child children]
      (let-weave (vector? child)
        [[child & args] child]
        [child child] :>>
          (do (.attachChild node child)
              (.addAll physics-space child))
        [args] :>> (let [{:as args-map} args]
                     (when (contains? args-map :kinematic?)
                       (.setKinematic (.getControl child PhysicsControl)
                                      (:kinematic? args-map)))))))))

(defn attach! [physics-node & children]
  (apply attach!* physics-node physics-node children))


(defn detach!
  "Detaches the children from both the scenegraph and the physics-space."
  [& spatials]
  (doseq [spatial spatials]
    (let [phys-control (.getControl spatial PhysicsControl)]
      (.setEnabled phys-control false))
    (.removeFromParent spatial)))
