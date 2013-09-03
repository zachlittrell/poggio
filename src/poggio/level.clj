(ns poggio.level
  (:import [com.jme3.bullet.control GhostControl RigidBodyControl]
           [com.jme3.bullet.collision.shapes BoxCollisionShape 
                                             MeshCollisionShape
                                             PlaneCollisionShape
                                             SimplexCollisionShape]
           [com.jme3.material Material]
           [com.jme3.math FastMath Plane Quaternion  Vector3f]
           [com.jme3.scene.shape Box Quad])
  (:use [data quaternion]
        [jme-clj assets
                 geometry
                 node
                 material
                 physics
                 transform]))

(defprotocol Level
  (load-level [this node warpables rotatables]))

(defrecord BasicLevel
  [loc dir wall-bounds wall-mat widgets]
  Level
  (load-level [this app warpables rotatables]
    (let [level-node (node*)
          [x z :as init] loc
          wall-mat (textured-material (asset-manager app) wall-mat)
          floor-rot  (angle->quaternion FastMath/PI :x)
          ceiling-rot (angle->quaternion 0 :x)
          quad (Quad. 16 16)
          quad-collision (MeshCollisionShape. quad)]
      (let [loc (Vector3f. (+ (* x 16) 0) -1.5 (+ (* 16 z) 0))
            dir (angle->quaternion dir :y)]
        (doseq [warpable warpables] (warp warpable loc))
        (doseq [rotatable rotatables] (rotate rotatable dir)))
      ;;Iteratively construct the floor
      (loop [places [init]
             seen   (into #{} wall-bounds)]
        (when-let [[[x z :as p] & more] (seq places)]
          (if (not (seen p))
            (do 
              (attach!* app level-node
                       (geom :shape quad
                             :material wall-mat
                             :local-translation (Vector3f. (- (* x 16) 8) 
                                                           -16 
                                                           (+ (* z 16) 8))
                             :local-rotation floor-rot
                             :controls [(RigidBodyControl. quad-collision
                                         0 )])
                        (geom :shape quad
                              :material wall-mat
                              :local-translation (Vector3f. (- (* x 16) 8)
                                                            0 
                                                            (- (* z 16) 8))
                              :local-rotation ceiling-rot
                              :controls [(RigidBodyControl.
                                           quad-collision
                                           0)]))
              (recur (concat (for [[x+ z+] [[1 0] [0 1] [-1 0] [0 -1]]]
                               [(+ x x+) (+ z z+)])
                               more)
                     (conj seen p)))
            (recur more (conj seen p)))))
      ;;Build the walls
      (let [collision-shape (BoxCollisionShape. (Vector3f. 8 8 8))]
        (doseq [[x z] wall-bounds]
          (attach!* app level-node
                   (geom :shape (Box. 8 8 8)
                         :material wall-mat
                         :move (Vector3f. (* x 16) -8 (* 16 z))
                         :controls [(RigidBodyControl. collision-shape
                                                       0)]))))
      ;;Build widgets
      (apply attach!* app level-node (for [widget widgets] (widget app)))
      level-node)))

(defn basic-level 
  ([m]
   (let [{:keys [loc dir walls wall-mat widgets]} m]
     (basic-level loc dir walls wall-mat widgets)))
  ([loc dir wall-bounds wall-mat widgets]
   (BasicLevel. loc dir wall-bounds wall-mat widgets)))
