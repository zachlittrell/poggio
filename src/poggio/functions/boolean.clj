(ns poggio.functions.boolean
  (:use [poggio.functions core parser utilities]))

(def true* (constantly* true (docstr [] "true")))
(def false* (constantly* false (docstr [] "false")))
(def not* (code-pog-fn ["b"] (docstr [["b" "a boolean"]]
                                     "the opposite of b.")
            "(if b true false)"))
