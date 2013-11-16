(ns poggio.functions.number
  (:require [clojure.math.numeric-tower :as math])
  (:use [control assert]
        [poggio.functions core parser utilities]))

(defn in-bounds? 
  [n min max]
  (and (<= min n)
       (>= max n)))

(defn integral?
  [n]
  (== (math/floor n) n))

(defmethod error-message integral?
  [_ [n]]
  (str "Expected integer number."))

(defmethod error-message in-bounds?
  [_ [n min max]]
  (str "Expected number between " min " and " max))

(def add* (fn->pog-fn +' "add" [{:name "x" :type Number}
                                {:name "y" :type Number}]
            (docstr [["x" "a number"] ["y" "a number"]]
                    "x + y")))
(def subtract* (fn->pog-fn -' "subtract" 
                  [{:name "x" :type Number} 
                   {:name "y" :type Number}]
                (docstr [["x" "a number"] ["y" "a number"]]
                    "x - y")))
(def multiply* (fn->pog-fn *' "multiply"
                  [{:name "x" :type Number}
                   {:name "y" :type Number}]
                (docstr [["x" "a number"] ["y" "a number"]]
                    "x * y")))
(def divide* (fn->pog-fn / "divide" 
                [{:name "x" :type Number} 
                 {:name "y" :type Number}]
                (docstr [["x" "a number"] ["y" "a number"]]
                  "x / y")))

(def mod* (fn->pog-fn rem "rem"
                 [{:name "x" :type Number} 
                 {:name "y" :type Number}]
                (docstr [["x" "a number"] ["y" "a number"]]
                  "the remainder of x/y")))

(def even?* (code-pog-fn ["n"]
              (docstr [["n" "an integer"]]
                      "true if n is even.")
              "(equal? (remainder n 2) 0)"))

(def odd?* (code-pog-fn ["n"]
              (docstr [["n" "an integer"]]
                      "true if n is odd.")
              "(not (even? n))"))
            

(def inc* (code-pog-fn ["n"]
              (docstr [["n" "a number"]]
                      "n + 1")
              "(add n 1)"))

(def dec* (code-pog-fn ["n"]
              (docstr [["n" "a number"]]
                  "n - 1")
              "(subtract n 1)"))

(def less-than?* (fn->pog-fn < "less-than?"
                   [{:name "x" :type Number}
                    {:name "y" :type Number}]
                 (docstr [["x" "a number"]
                          ["y" "a number"]]
                   "true if x < y")))

(def greater-than?* (fn->pog-fn > "greater-than?"
                      [{:name "x" :type Number}
                       {:name "y" :type Number}]
                      (docstr [["x" "a number"]
                               ["y" "a number"]]
                        "true if x > y")))
