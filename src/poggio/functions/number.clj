(ns poggio.functions.number
  (:use [poggio.functions core]))

(def add* (fn->pog-fn +' "add" ["n1" "n2"]))
(def subtract* (fn->pog-fn -' "subtract" ["n1" "n2"]))
(def multiply* (fn->pog-fn *' "multiply" ["n1" "n2"]))
(def divide* (fn->pog-fn / "divide" ["n1" "n2"]))

(def inc* (seq->pog-fn "inc" ["n"]
              (list add* (var* "n") (constantly* 1))))

(def dec* (seq->pog-fn "dec" ["n"]
              (list subtract* (var* "n") (constantly* 1))))

(def less-than* (fn->pog-fn < "less-than?"
                      ["n1" "n2"]))

(def greater-than* (fn->pog-fn < "greater-than?"
                      ["n1" "n2"]))
