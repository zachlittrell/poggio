(ns poggio.functions.boolean
  (:use [poggio.functions core parser utilities]))

(def true* (constantly* true (docstr [] "true")))
(def false* (constantly* false (docstr [] "false")))
(def not* (code-pog-fn ["b"] (docstr [["b" "a boolean"]]
                                     "the opposite of b.")
            "(if b true false)"))

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

