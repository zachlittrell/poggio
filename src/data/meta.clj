(ns data.meta
  "Functions for handling Clojure metadata")

(defprotocol MetaObj
  (meta* [p] "Returns the map of the metadata for this provider."))

(extend-protocol MetaObj
  clojure.lang.IMeta
  (meta* [m] (meta m)))

(defn merge-meta [obj metadata]
  "Merges the current metadata on this object with
   provided metadata"
  (with-meta obj (merge (meta obj) metadata)))

