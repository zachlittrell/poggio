(ns data.map
  "Functions for modifying Clojure maps
   and sequences of pairs."
  (:refer-clojure :exclude [keys merge vals])
  (:require [clojure.core :as core]))

(defn seq->pairs [s]
  "Converts a sequence into a sequence of vector pairs,
   maintaining the order they appeared in s."
  (map vec (partition 2 s)))

(defprotocol MapLike
  "Protocol for operations on map-like structures"
  (keys [m] "Returns the keys of m.")
  (vals [m] "Returns the values of m.")
  (key-map [m key-fn] "Returns a MapLike copy of m with
                       key-fn mapped on m's keys.")
  (value-map [m val-fn] "Returns a MapLike copy of m with
                         val-fn mapped on m's values.")
  (key-filter [m key-pred] "Returns a MapLike copy of m, filtering
                            m's keys with key-pred.")
  (value-filter [m val-pred] "Returns a MapLike copy of m, filtering
                              m's values with val-pred.")
  (merge [m1 m2] "Merges m1 with m2, such that in the case where
                  m1 and m2 have the same key, m2's key-value pair
                  overwrites m1's respective pair."))

(extend-protocol MapLike
  nil
  (keys [_] nil)
  (vals [_] nil)
  (key-map [_ _] nil)
  (value-map [_ _] nil)
  (key-filter [_ _] nil)
  (value-filter [_ _] nil)
  (merge [_ m] m)

  clojure.lang.ISeq
  (keys [m] (map first m))
  (vals [m] (map second m))
  (key-map [m key-fn] (map (juxt (comp key-fn first) second) m))
  (value-map [m val-fn] (map (juxt first (comp val-fn second)) m))
  (key-filter [m key-pred] (filter (comp key-pred first) m))
  (value-filter [m val-pred] (filter (comp val-pred second) m))
  (merge [m1 m2]
          (let [keys (into #{} (keys m2))]
            (concat (filter (comp not keys first) m1)
                    m2)))

  clojure.lang.IPersistentMap
  (keys [m] (core/keys m))
  (vals [m] (core/vals m))
  (key-map [m key-fn] (into {} (key-map (seq m) key-fn)))
  (value-map [m val-fn] (into {} (value-map (seq m) val-fn)))
  (key-filter [m key-pred] (into {} (key-filter (seq m) key-pred)))
  (value-filter [m val-pred] (into {} (value-filter (seq m) val-pred)))
  (merge [m1 m2] (core/merge m1 m2)))

(defn intersecting-merge
  "Returns m merged with the maps in more,
   but only by replacing keys that already exist in m."
  [m & more]
  (let [in-shared-keys (into #{} (keys m))]
    (loop [m m
           more more]
      (if-let [[m2 & ms] more]
        (recur (merge m (key-filter m2 in-shared-keys)) ms)
        m))))
