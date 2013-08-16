(ns data.object
  (:use [control assert]
        [data.coll :only [zip zip-with distinct-by]])
  (:import [java.lang.reflect Method Modifier]))

(defprotocol Implementable
  (implements? [p child] "Returns true if child implements p."))

(extend-protocol Implementable
  Class
  (implements? [p child] (instance? p child))
  clojure.lang.PersistentArrayMap
  (implements? [p child] (extends? p (class child))))

(defrecord UnionImpl
  [types]
  Implementable
  (implements? [p child] (some #(implements? % child) types)))

(defrecord IntersectionImpl
  [types]
  Implementable
  (implements? [p child] (every? identity (zip-with implements?
                                                    types
                                                    (repeat child)))))

(defn type-mismatches [types objs]
  "Returns either nil if all objects in objs 'implement' the corresponding
   type in types, as per the Implementable protocol, or the first pair
   that fails this type check."
  (seq (filter (fn [[type obj]] (not (implements? type obj)))
          (zip types objs))))

(defn check-types! [types objs]
  (doseq [[type obj] (zip types objs)]
    (assert! (implements? type obj))))

(defprotocol GeneralTypeStringable
  (general-type-str [type]))

(extend-protocol GeneralTypeStringable
  Class
  (general-type-str [c] (.getSimpleName c))
  clojure.lang.PersistentArrayMap
  (general-type-str [p] (:name (meta (:var p)))))

(defmulti type-str identity)

(defmethod type-str :default [type]
  (general-type-str type))

(defn obj-type-str [obj]
  (type-str (type obj)))

(defmethod error-message implements? [f [type child]]
  (format "Expected type %s. Received type %s"
          (type-str type)
          (obj-type-str child)))

