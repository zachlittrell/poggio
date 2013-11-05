(ns poggio.functions.boolean
  (:use [poggio.functions core parser utilities]))

(def true* (constantly* true (docstr [] "true")))
(def false* (constantly* false (docstr [] "false")))
(def not* (code-pog-fn ["b"] (docstr [["b" "a boolean"]]
                                     "the opposite of b.")
            "(if b false true)"))

(def and* (code-pog-fn ["b1" "b2"]
              (docstr [["b1" "a boolean"]
                       ["b2" "a boolean"]]
                "true if b1 and b2 are true.")
              "(if b1 b2 false)"))

(def or* (code-pog-fn ["b1" "b2"]
              (docstr [["b1" "a boolean"]
                       ["b2" "a boolean"]]
                "true if b1 or b2 are true.")
              "(if b1 true b2)"))

(def and** (code-pog-fn ["bs"]
              (docstr [["bs" "a list of booleans"]]
                "true if for all booleans in bs are true.")
             "(empty? (drop-while id bs))"))

(def or** (code-pog-fn ["bs"]
              (docstr [["bs" "a list of booleans"]]
                "true if at least one boolean in bs is true.")
            "(any? id bs)"))
