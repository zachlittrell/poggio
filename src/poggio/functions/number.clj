(ns poggio.functions.number
  (:use [poggio.functions core parser utilities]))

(def add* (fn->pog-fn +' "add" ["x" "y"]
            (docstr [["x" "a number"] ["y" "a number"]]
                    "x + y")))
(def subtract* (fn->pog-fn -' "subtract" 
                  ["x" "y"]
                (docstr [["x" "a number"] ["y" "a number"]]
                    "x - y")))
(def multiply* (fn->pog-fn *' "multiply"
                  ["x" "y"]
                (docstr [["x" "a number"] ["y" "a number"]]
                    "x * y")))
(def divide* (fn->pog-fn / "divide" ["x" "y"]
                (docstr [["x" "a number"] ["y" "a number"]]
                  "x / y")))

(def inc* (code-pog-fn ["n"]
              (docstr [["n" "a number"]]
                      "n + 1")
              "add n 1"))

(def dec* (code-pog-fn ["n"]
              (docstr [["n" "a number"]]
                  "n - 1")
              "subtract n 1"))

(def less-than* (fn->pog-fn < "less-than?"
                      ["x" "y"]
                 (docstr [["x" "a number"]
                          ["y" "a number"]]
                   "true if x < y")))

(def greater-than* (fn->pog-fn < "greater-than?"
                      ["x" "y"]
                      (docstr [["x" "a number"]
                               ["y" "a number"]]
                        "true if x > y")))
