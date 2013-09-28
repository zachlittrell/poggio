(ns data.maybe
  "Functions that operate with a Maybe/Option type.")

(defprotocol Maybe)

(defrecord Just [just]
  Maybe)

(def nothing (reify Maybe))

(defn nothing? [m]
  (identical? m nothing))

(defn just?
  "Returns true iff m is not nothing."
  [m]
  (not (nothing? m)))


