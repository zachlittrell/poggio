(ns poggio.functions.value
  (:use [poggio.functions core]))

(def id* (seq->pog-fn "id" ["x"] "x"))
(def equals* (fn->pog-fn = "equals" ["o1" "o2"]))