(ns poggio.functions.value
  (:use [poggio.functions core parser utilities]))

(def id* (code-pog-fn ["x"]
                (docstr [["x" "a value"]]
                        "x")
            "x"))
(def equal?* (fn->pog-fn == "equal?" [{:name "o1"
                                      :type Number}
                                     {:name "o2"
                                      :type Number}]))
