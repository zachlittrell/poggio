(ns poggio.level
  (:import [com.jme3.bullet.control GhostControl RigidBodyControl]
           [com.jme3.bullet.collision.shapes BoxCollisionShape 
                                             MeshCollisionShape
                                             PlaneCollisionShape
                                             SimplexCollisionShape]
           [com.jme3.material Material]
           [com.jme3.math FastMath Plane Quaternion Vector2f Vector3f]
           [com.jme3.scene.shape Box Quad])
  (:use [jme-clj geometry
                 physics]))

(defprotocol Level
  (load-level [this node player]))

(defrecord BasicLevel
  [player-loc wall-bounds wall-mat]
  Level
  (load-level [this app player]
    (let [[x z :as init] player-loc
          floor-rot (doto (Quaternion.)
                      (.fromAngleNormalAxis (* -90 FastMath/DEG_TO_RAD)
                                            Vector3f/UNIT_X))
          ceiling-rot (doto (Quaternion.)
                        (.fromAngleNormalAxis (* 90 FastMath/DEG_TO_RAD)
                                              Vector3f/UNIT_X))
          quad (Quad. 4 4)
          quad-collision (MeshCollisionShape. quad)]
      (.warp player (Vector3f. (+ (* x 4) 0) -2 (+ (* 4 z) 0)))
      ;;Iteratively construct the floor
      (loop [places [init]
             seen   (into #{} wall-bounds)]
        (when-let [[[x z :as p] & more] (seq places)]
          (if (not (seen p))
            (do 
              (attach! app
                       (geom :shape quad
                             :material wall-mat
                             :local-translation (Vector3f. (- (* x 4) 2) 
                                                           -4 
                                                           (+ (* z 4) 2))
                             :local-rotation floor-rot
                             :controls [(RigidBodyControl. quad-collision
                                         0 )])
                        (geom :shape quad
                              :material wall-mat
                              :local-translation (Vector3f. (- (* x 4) 2)
                                                            0 
                                                            (- (* z 4) 2))
                              :local-rotation ceiling-rot
                              :controls [(GhostControl.
                                           quad-collision
                                           )]))
              (recur (concat (for [[x+ z+] [[1 0] [0 1] [-1 0] 
                                            [0 -1] [1 1] [-1 -1]]]
                               [(+ x x+) (+ z z+)])
                               more)
                     (conj seen p)))
            (recur more (conj seen p))))))
    ;;Build the walls
    (let [collision-shape (BoxCollisionShape. (Vector3f. 2 2 2))]
      (doseq [[x z] wall-bounds]
        (attach! app
                 (geom :shape (Box. 2 2 2)
                       :material wall-mat
                       :move (Vector3f. (* x 4) -2 (* 4 z))
                       :controls [(RigidBodyControl. collision-shape
                                                     0)]))))))

(defn basic-level [player-loc wall-bounds wall-mat]
  (BasicLevel. player-loc wall-bounds wall-mat))
