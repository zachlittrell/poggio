(ns data.vector
  "Functions for dealing with vector-like objects."
  (:import [com.jme3.math Vector2f Vector3f Vector4f]))

(defn vec->vecf
  "Converts a clojure vector into a jme vector."
  [v]
  (condp == (count v)
    2 (Vector2f. (v 0) (v 1))
    3 (Vector3f. (v 0) (v 1) (v 2))
    4 (Vector4f. (v 0) (v 1) (v 2) (v 3))))

(defn vecf->vec
  "Converts a jme vector into a clojure vector."
  [vf]
  (apply vector-of :float (.toArray vf)))

(defprotocol VectorfProvider
  (vectorf [v] "Returns a Vector3f"))

(extend-protocol VectorfProvider
  Vector2f
  (vectorf [v] v)
  Vector3f
  (vectorf [v] v)
  Vector4f
  (vectorf [v] v)
  clojure.lang.IPersistentVector
  (vectorf [v] (vec->vecf v)))

