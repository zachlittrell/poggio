(ns data.quaternion
  (:import [com.jme3.math FastMath Quaternion Vector3f]))

(defn angle->quaternion [angle normal-vector]
  "Returns the quaternion rotated by angle about the axis represented by
   the keyword normal vector, which can be :x, :y, or :z."
  (.fromAngleNormalAxis (Quaternion.)
                        (+ angle FastMath/HALF_PI)
                        (condp = normal-vector
                          :x Vector3f/UNIT_X
                          :y Vector3f/UNIT_Y
                          :z Vector3f/UNIT_Z)))

(defn quaternion->direction-vector [quaternion]
  "Returns the direction of the quaternion."
  (.getRotationColumn quaternion 2))
