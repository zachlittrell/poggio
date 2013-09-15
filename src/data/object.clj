(ns data.object
  (:use [control assert]
        [data.coll :only [zip zip-with distinct-by]])
  (:require [clojure.string :as str])
  (:import [java.lang.reflect Method Modifier]))

(defprotocol Implementable
  (implements? [p child] "Returns true if child implements p."))

(extend-protocol Implementable
  Class
  (implements? [p child] (instance? p child))
  clojure.lang.PersistentArrayMap
  (implements? [p child] (extends? p (class child))))


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

(defprotocol ObjTypeStringable
  (obj-type-str [obj]))

(extend-protocol ObjTypeStringable
  Object
  (obj-type-str [obj] (type-str (type obj))))

(defmethod type-str clojure.lang.Seqable [_]
  "list")

(defmethod type-str BigDecimal [_]
  "number")

(defmethod type-str Boolean [_]
  "boolean")


(defmethod error-message implements? [f [type child]]
  (format "Expected type %s. Received type %s"
          (type-str type)
          (obj-type-str child)))

(defrecord UnionImpl
  [types]
  GeneralTypeStringable
  (general-type-str [u] (str/join " or " (map type-str types)))
  Implementable
  (implements? [p child] (some #(implements? % child) types)))

(defn union-impl [& types]
  (UnionImpl. types))

(defrecord IntersectionImpl
  [types]
  Implementable
  (implements? [p child] (every? identity (zip-with implements?
                                                    types
                                                    (repeat child)))))

