(ns jme-clj.collision
  (:import [com.jme3.collision CollisionResults]
           [com.jme3.math Ray]))

(defn closest-collision 
  "Returns the closest collision between collidable and other,
   or nil if there is none."
  ([collidable other]
    (let [results (CollisionResults.)]
      (.collideWith other collidable results)
      (when (> (.size results) 0)
        (.getClosestCollision results)))))

(defn closest-collision-from-camera
  "Returns the closest collision between collidable and camera,
   or nil if there is none."
  ([camera collidable]
    (closest-collision collidable (Ray. (.getLocation camera)
                                        (.getDirection camera)))))
 

