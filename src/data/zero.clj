(ns data.zero)

(defprotocol Zeroable
  "Protocol for types that have a '0', or identity."
  (zero [z]))

(extend-protocol Zeroable
  Number (zero [z] 0)
  String (zero [z] "")
  clojure.lang.IPersistentCollection (zero [z] (empty z)))
