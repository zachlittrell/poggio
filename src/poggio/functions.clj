(ns poggio.functions)

(defprotocol PogFn
  "Protocol for functions in Poggio."
  (parameters [f] "Returns a sequence of parameter names.")
  (invoke [f args] "Returns the result of applying args to f."))

(defrecord BasicPogFn [parameters invoke-fn]
  PogFn
  (parameters [f] parameters)
  (invoke [f args] (invoke-fn args)))

(defn fn->pog-fn [f name parameters]
  "Returns a PogFn wrapping around a Clojure function."
  (BasicPogFn. parameters
               (partial apply f)))

(def if* (fn->pog-fn (fn [[pred true-fn false-fn]]
                       (if (invoke pred [])
                         (invoke true-fn [])
                         (invoke false-fn [])))
                          "if" ["pred" "true-fn" "false-fn"]))

(defn comp* [f1 f2]
  "Composes two PogFunctions."
  (BasicPogFn. (parameters f2)
               #(invoke f1 [(invoke f2 %)])))

