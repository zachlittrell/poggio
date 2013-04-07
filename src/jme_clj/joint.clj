(ns jme-clj.joint
  (:use [control defutilities]
        [data quaternion])
  (:import [com.jme3.bullet.joints HingeJoint]))

(defmacro def-joint-constructor [name joint-class]
  (let [constructor `(new ~joint-class ~'nodeA ~'nodeB ~'pivotA ~'pivotB
                          ~'axisA ~'axisB)]
  `(def-opts-constructor ~name
      [:setter]
      {:nodeA
       :nodeB nil
       :pivotA nil
       :pivotB nil
       :axisA nil
       :axisB nil}
      ~constructor
     {:nodeA [:no-op]
      :nodeB [:no-op]
      :pivotA [:no-op]
      :pivotB [:no-op]
      :axisA [:no-op]
      :axisB [:no-op]})))

(def-joint-constructor hinge-joint HingeJoint)
(def-joint-constructor slider-joint SliderJoint)

(defprotocol Motorizable
  (disable! [m] "Disables the motorizable.")
  (enable! [m lower-limit upper-limit velocity max-motor-force] 
           "Enables the motorizable"))

(extend-protocol Motorizable
  HingeJoint
  (disable! [m] (.enableMotor m false 0 0))
  (enable! [m lower-limit upper-limit velocity max-motor-force]
           (.setLimit m lower-limit upper-limit)
           (.enableMotor m true velocity max-motor-force))
  SliderJoint
  (disable! [m] (.setPoweredLinMotor false))
  (enable! [m lower-limit upper-limit velocity max-motor-force]
      (doto m
        (.setLowerLinLimit lower-limit)
        (.setUpperLinLimit upper-limit)
        (.setTargetLinMotorVelocity velocity)
        (.setMaxLinMotorForce max-motor-force)
        (.setPoweredLinMotor true))))
