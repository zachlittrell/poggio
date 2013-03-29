(ns poggio.functions.core
  (:use [control bindings]
        [data coll]))

(defprotocol PogFn
  "Protocol for functions in Poggio."
  (parameters [f] "Returns a sequence of parameter names.")
  (invoke [f args] "Returns the result of applying args to f."))

(defn is-pog-fn?
  "Returns true if the object is a PogFn. Can optionally
   specify what arity the PogFn ought to be."
  ([obj]
   (extends? PogFn (class obj)))
  ([obj arity]
   (and (is-pog-fn? obj)
        (== arity (count (parameters obj))))))

(defn pog-fn [parameters invoke-fn]
  "Returns a PogFn that returns parameters and directly
   passes the arguments of invoke to invoke-fn."
  (reify PogFn
    (parameters [f] parameters)
    (invoke [f args] (invoke-fn f args))))

(defn basic-pog-fn [parameters invoke-fn]
  "Returns a PogFn that returns parameters and passes
   just args from invoke to invoke-fn."
  (reify PogFn
    (parameters [f] parameters)
    (invoke [f args] (invoke-fn args))))

(defn partial* [f args-map]
  "Partially applies a Pog function using args-map, which
   maps applied parameters to their values."
  (basic-pog-fn (filter (comp not (partial contains? args-map)) (parameters f))
               #(loop [params (parameters f)
                       args   %
                       final-args []]
                  (if-let [[param & params] (seq params)]
                    (if-let [arg (args-map param)]
                      (recur params args (conj final-args arg))
                      (recur params (next args) (conj final-args (first args))))
                    (invoke f final-args)))))

(defn value [v]
  "Returns (invoke v []) if v is a nullary PogFn,
   otherwise just v."
  (if (is-pog-fn? v 0)
    (invoke v [])
    v))

(defn invoke* [f args]
  "Invokes clojure function f on args, reducing any nullary
   PogFn into its output."
  (apply f (map value args)))

(defn fn->pog-fn [f name parameters]
  "Returns a PogFn wrapping around a Clojure function."
  (basic-pog-fn parameters
               (partial invoke* f)))

(defn seq->partial-pog-fn [seq]
  "Converts seq into an applied PogFn. Unlike seq->pog-fn,
   all elements must be already PogFns."
  (if (seq? seq)
    (let [[f & args] seq]
      (partial* f (zipmap (parameters f)
                          (map seq->partial-pog-fn args))))
    seq))

(defn invoke-seq 
  "Invokes the function represented by the seq, with the first argument
   as the function, followed by its arguments. Will recursively invoke
   any nested function-sequences."
  ([seq]
   (invoke (seq->partial-pog-fn seq) []))
  ([seq parameters args]
    (if (string? seq) (value (second args))
      (let [param->arg (zipmap parameters args)]
      (invoke-seq (nested-leaf-map #(if-let [arg (param->arg %)]
                                     arg
                                      %)
                                  seq))))))


(defn seq->pog-fn
  "Returns a PogFn that invokes the function represented by seq.
   See invoke-seq for details"
  ([name params seq]
   (pog-fn params
           (fn [f args]
             (invoke-seq seq (cons name (parameters f))
                             (cons f args))))))
                          


(def if* (basic-pog-fn ["pred" "true-fn" "false-fn"]
                       (fn [[pred true-fn false-fn]]
                         (if (invoke pred [])
                           (value true-fn)
                           (value false-fn)))))
  
(defn constantly* [obj]
  (fn->pog-fn (constantly obj) (str obj) []))

(defn comp* [f1 f2]
  "Composes two PogFunctions."
  (seq->pog-fn "compose" ["f" "g" "x"] (list "f" (list "g" "x"))))
