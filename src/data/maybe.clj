(ns data.maybe
  "Functions that operate with a Maybe/Option type.")

(defrecord Just [just])

(defrecord NothingType [])

(def nothing (NothingType.))

(defn nothing? [m]
  (identical? m nothing))

(defn just? [m]
  "Returns true iff m is not nothing."
  (not (nothing? m)))


