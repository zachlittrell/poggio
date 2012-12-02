(ns data.metadata
  "Functions for handling Clojure metadata")

(defn merge-metadata [obj metadata]
  "Merges the current metadata on this object with
   provided metadata"
  (with-meta obj (merge (meta obj) metadata)))
