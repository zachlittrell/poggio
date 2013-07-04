(ns poggio.functions.core
  (:use [control bindings]
        [data coll object string]
        [poggio.functions utilities]))

(defprotocol PogFn
  "Protocol for functions in Poggio."
  (docstring [f] "Returns the documentation of this function.")
  (parameters [f] "Returns a sequence of parameters.
                   Parameters must implement the PogFnParameter 
                   protocol.")
  (invoke [f args] "Returns the result of applying args to f."))

(defprotocol PogFnParameter
  "Protocol for parameters of a PogFn."
  (name* [param] "Returns the name of the parameter" )
  (type* [param] "Returns the type of the parameter."))

(extend-protocol PogFnParameter
  java.lang.String
  (name* [s] s)
  (type* [s] Object)
  clojure.lang.ILookup
  (name* [param] (:name param))
  (type* [param] (:type param)))

(defn pog-implements? [param obj]
  "Returns true if obj 'implements' the type of the PogFnParameter."
  (implements? (type* param) obj))

(defn is-pog-fn?
  "Returns true if the object is a PogFn. Can optionally
   specify what arity the PogFn ought to be."
  ([obj]
   (extends? PogFn (class obj)))
  ([obj arity]
   (and (is-pog-fn? obj)
        (== arity (count (parameters obj))))))

(defprotocol ScopedPogFn
  "Protocol for PogFn that takes a map of variables.
   It is assumed if you extend ScopedPogFn, you also
   extend PogFn."
  (scoped-invoke [f env]))

    
(defrecord PogFnType [parameter-count]
  Implementable
  (implements? [p child]
    (is-pog-fn? child 2)))


(defn docstring* [f]
  "Returns the docstring of f with whitespace merged together."
  (trivialize-whitespace (docstring f)))

(defn checked-invoke [f args]
  "If the types of args match the types of f's parameters, then 
   returns the result of invoking f on args. Else, it throws an exception."
  (check-types! (map type* (parameters f)) args)
  (invoke f args))

(defn pog-fn 
  "Returns a PogFn that returns parameters and directly
   passes the arguments of invoke to invoke-fn."
  ([parameters invoke-fn]
   (pog-fn parameters "" invoke-fn))
  ([parameters docstring invoke-fn]
    (reify PogFn
      (docstring [f] docstring)
      (parameters [f] parameters)
      (invoke [f args] (invoke-fn f args)))))

(defn basic-pog-fn 
  "Returns a PogFn that returns parameters and passes
   just args from invoke to invoke-fn."
  ([parameters invoke-fn]
   (basic-pog-fn parameters "" invoke-fn))
  ([parameters docstring  invoke-fn]
    (reify PogFn
      (docstring [f] docstring)
      (parameters [f] parameters)
      (invoke [f args] (invoke-fn args)))))

(defmacro scoped-pog-fn [parameters docstring scope-bindings & body]
  "Returns a PogFn that is also a ScopedPogFn. invoke delegates to
   scoped-invoke, putting the parameters into the scope."
  `(reify
    ScopedPogFn
    (scoped-invoke ~scope-bindings
      ~@body)
    PogFn
    (docstring [f#] ~docstring)
    (parameters [f#] ~parameters)
    (invoke [f# args#]
      (scoped-invoke f# (zipmap (map name* ~parameters) args#)))))

(defn partial* [f args-map]
  "Partially applies a Pog function using args-map, which
   maps applied parameters to their values."
  (basic-pog-fn (filter (comp not (partial contains? args-map)) 
                        (map name* (parameters f)))
               #(loop [params (map name* (parameters f))
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

(defn fn->pog-fn 
  "Returns a PogFn wrapping around a Clojure function."
  ([f name parameters]
   (fn->pog-fn f name parameters ""))
  ([f name parameters docstring]
    (basic-pog-fn parameters docstring
                 (partial invoke* f))))

(defn seq->partial-pog-fn [seq]
  "Converts seq into an applied PogFn. Unlike seq->pog-fn,
   all elements must be already PogFns."
  (if (seq? seq)
    (let [[f & args] seq]
      (partial* f (zipmap (map name* (parameters f))
                          (map seq->partial-pog-fn args))))
    seq))

(defn invoke-seq 
  "Invokes the function represented by the seq, with the first argument
   as the function, followed by its arguments. Will recursively invoke
   any nested function-sequences."
  ([seq]
   (invoke (seq->partial-pog-fn seq) []))
  ([seq env]
   (invoke-seq (nested-leaf-map #(or (env %) %) seq)))
  ([seq parameters args]
     (invoke-seq seq parameters args {}))
  ([seq parameters args env]
    (let [param->arg (merge env (zipmap (map name* parameters) args))]
      (if (string? seq)
        (or (value (param->arg seq)))
            ;;TODO Here we'll throw an exception
        (invoke-seq seq param->arg)))))


(defn seq->pog-fn
  "Returns a PogFn that invokes the function represented by seq.
   See invoke-seq for details"
  ([name params seq]
   (seq->pog-fn name params "" seq))
  ([name params docstring seq]
   (scoped-pog-fn params docstring
      [f env]
      (invoke-seq seq (assoc env name f)))))


(def if* (basic-pog-fn ["predicate" "true-fn" "false-fn"]
                       (fn [[pred true-fn false-fn]]
                         (if (invoke pred [])
                           (value true-fn)
                           (value false-fn)))))
(def let* (scoped-pog-fn ["var" "val" "function"]
                         (docstr [["var" "a variable"]
                                  ["val" "a value"]
                                  ["function" "a function to execute"]]
                                 "function executed with variable set to value")
                         [f env]
                         (if (implements? ScopedPogFn (env "function"))
                           (scoped-invoke (env "function")
                                          (assoc env (env "var") (env "val")))
                           (value (env "function")))))

(defn constantly* [obj]
  (fn->pog-fn (constantly obj) "constant"  []))

(defn comp* [f1 f2]
  "Composes two PogFunctions."
  (seq->pog-fn "compose" ["f" "g" "x"] (list "f" (list "g" "x"))))
