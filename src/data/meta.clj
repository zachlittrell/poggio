(ns data.meta
  "Functions for handling Clojure metadata")

(defn merge-meta [obj metadata]
  "Merges the current metadata on this object with
   provided metadata"
  (with-meta obj (merge (meta obj) metadata)))

