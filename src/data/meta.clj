(ns data.meta
  "Functions for handling Clojure metadata")

(defn merge-meta
  "Merges the current metadata on this object with
   provided metadata"
  [obj metadata]
  (with-meta obj (merge (meta obj) metadata)))

