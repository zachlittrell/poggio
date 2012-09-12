(ns data.map)

(defn key-map
  "Returns a new map with key-fn applied to each key in m."
  [key-fn m]
  (into {} (map (juxt (comp key-fn first) second) m)))

(defn value-map
  "Returns a new map with val-fn applied to each value in m."
  [val-fn m]
  (into {} (map (juxt first (comp val-fn second)) m)))

(defn intersecting-merge
  "Returns m merged with the maps in more,
   but only by replacing keys that already exist in m."
  [m & more]
  (let [keys (keys m)]
    (loop [m m
           more more]
      (if-let [[m2 & ms] more]
        (recur (merge m (select-keys m2 keys)) ms)
        m))))
