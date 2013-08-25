(ns poggio.functions.value
  (:use [poggio.functions core parser utilities]))

(def id* (code-pog-fn ["x"]
                (docstr [["x" "a value"]]
                        "x")
            "x"))
(def equals* (fn->pog-fn = "equals" ["o1" "o2"]))
