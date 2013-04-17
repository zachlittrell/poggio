(ns data.object
  (:use [data.coll :only [zip zip-with distinct-by]])
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
