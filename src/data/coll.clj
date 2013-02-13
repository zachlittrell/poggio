(ns data.coll)

(defn distinct-by [f coll]
  "Returns coll with only distinct elements, where two elements are 
   equal if they have the same output from f."
  (map first (vals (group-by f coll))))
