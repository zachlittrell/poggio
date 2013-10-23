(ns data.bit)

(defn contains-bitmask?
  [num mask]
  (-> (bit-and num mask)
      (zero?)
      (not)))
