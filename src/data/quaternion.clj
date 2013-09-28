(ns data.quaternion
  (:import [com.jme3.math FastMath Quaternion Vector3f]))

(defn angle->quaternion
  "Returns the quaternion rotated by angle about the axis represented by
   the keyword normal vector, which can be :x, :y, or :z."
  [angle normal-vector]
  (.fromAngleNormalAxis (Quaternion.)
                        (+ angle FastMath/HALF_PI)
                        (condp = normal-vector
                          :x Vector3f/UNIT_X
                          :y Vector3f/UNIT_Y
                          :z Vector3f/UNIT_Z)))

(defn quaternion->direction-vector
  "Returns the direction of the quaternion."
  [quaternion]
  (.getRotationColumn quaternion 2))

(defn clamp-angle [angle]
  (let [[_ angle] (apply min-key first (for [angle* [0 
                                               FastMath/HALF_PI
                                               FastMath/PI
                                               (- FastMath/HALF_PI)
                                               (- FastMath/PI)]
                                             :let [[ang1 ang2] (sort [angle
                                                                      angle*])]]
                                   [(Math/abs (- ang1 ang2))
                                    angle*]))]
    angle))
