(ns poggio.functions
  (:use [control bindings]))

(defprotocol PogFn
  "Protocol for functions in Poggio."
  (parameters [f] "Returns a sequence of parameter names.")
  (invoke [f args] "Returns the result of applying args to f."))

(defn invoke* [f args]
  (apply f  (for [arg args]
              (if (and (extends? PogFn (class arg))
                       (empty? (parameters arg)))
               (invoke arg [])
               arg))))

(defn invoke-seq [s parameters args]
  )


(defrecord BasicPogFn [parameters invoke-fn]
  PogFn
  (parameters [f] parameters)
  (invoke [f args] (invoke-fn args)))

(defn fn->pog-fn [f name parameters]
  "Returns a PogFn wrapping around a Clojure function."
  (BasicPogFn. parameters
               (partial invoke* f)))

(defn seq->pog-fn [seq parameters]
  (BasicPogFn. parameters
               (partial invoke-seq seq)))


(def if* (fn->pog-fn (fn [[pred true-fn false-fn]]
                       (if (invoke pred [])
                         (invoke true-fn [])
                         (invoke false-fn [])))
                          "if" ["pred" "true-fn" "false-fn"]))

(defn comp* [f1 f2]
  "Composes two PogFunctions."
  (BasicPogFn. (parameters f2)
               #(invoke f1 [(invoke f2 %)])))

(defn partial* [f args-map]
  "Partially applies a Pog function."
  (BasicPogFn. (filter (comp not (partial contains? args-map)) (parameters f))
               #(loop [params (parameters f)
                       args   %
                       final-args []]
                  (if-let [[param & params] (seq params)]
                    (if-let [arg (args-map param)]
                      (recur params args (conj final-args arg))
                      (recur params (next args) (conj final-args (first args))))
                    (invoke f final-args)))))
