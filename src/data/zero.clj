(ns data.zero)

(defprotocol Zeroable
  "Protocol for types that have a '0', or identity."
  (zero [z] "Returns the zero object for this type.")
  (zero! [z] "Returns the object given set to be equivalent to the zero object.
              Note, this operation isn't always supported."))

(extend-protocol Zeroable
  Number (zero [z] 0)
  String (zero [z] "")
  clojure.lang.IPersistentCollection (zero [z] (empty z))
  com.jme3.math.Vector3f 
  (zero [v] (com.jme3.math.Vector3f/ZERO))
  (zero! [v] (.zero v)))
