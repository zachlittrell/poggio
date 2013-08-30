(ns poggio.functions.core
  (:require [data.map :as map])
  (:use [control assert bindings io]
        [data coll object string]
        [poggio.functions utilities]))

(defprotocol PogFn
  "Protocol for functions in Poggio."
  (docstring [f] "Returns the documentation of this function.")
  (parameters [f] "Returns a sequence of parameters.
                   Parameters must implement the PogFnParameter 
                   protocol.")
  (invoke [f args] "Returns the result of invoke f with the given args"))

(extend-protocol PogFn
  Object
  (parameters [_] [])
  (invoke [f env] f))

(defprotocol LazyPogFn
  "Protocol for functions that take their arguments outside of the scope."
  (lazy-invoke [f env args]))

(defprotocol Sourceable 
  "Protocol for functions that give their source code."
  (source-code [f] "Returns the source code as a string, or nil if there is no
                    source code."))

(defn source* [f]
  "Returns the source code of f, if it has any. Else, it returns the empty
   string."
  (if (implements? Sourceable f)
    (or (source-code f) "")
    ""))

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

(defn fn->str [f]
  (str (count (parameters f)) "-parameter function."))

(defrecord PogFnType [parameter-count]
  Implementable
  (implements? [p child]
    (is-pog-fn? child 2)))

(defn docstring* [f]
  "Returns the docstring of f with whitespace merged together."
  (trivialize-whitespace (docstring f)))

(defn pog-fn 
  "Returns a PogFn that returns parameters and directly
   passes the arguments of invoke to invoke-fn."
  ([parameters invoke-fn]
   (pog-fn parameters "" invoke-fn))
  ([parameters docstring invoke-fn]
    (reify 
      ObjTypeStringable
      (obj-type-str [f] (fn->str))
      PogFn
      (docstring [f] docstring)
      (parameters [f] parameters)
      (invoke [f env] (invoke-fn f env)))))

(defn basic-pog-fn 
  "Returns a PogFn that returns parameters and passes
   just args from invoke to invoke-fn."
  ([parameters invoke-fn]
   (basic-pog-fn parameters "" invoke-fn))
  ([parameters docstring  invoke-fn]
    (reify 
      ObjTypeStringable
      (obj-type-str [f] (fn->str f))
      PogFn
      (docstring [f] docstring)
      (parameters [f] parameters)
      (invoke [f env] 
              (invoke-fn env)))))

(def variable-defined? contains?)

(defmethod error-message variable-defined? [f [env symbol-name]]
  (str symbol-name " is not defined."))

(declare value)
(defrecord PogFnVar [name]
  PogFn
  (docstring [f] "")
  (parameters [f] [])
  (invoke [f env]
    (lazy-invoke f env env))
  LazyPogFn
  (lazy-invoke [f env _] 
    (assert! (variable-defined? env name))
    (value (env name) env)))

(defn var* [name]
  "Returns a PogFnVar for name."
  (PogFnVar. name))

(defn var*? [o]
  (implements? PogFnVar o))

(defn correct-arity? [f args]
  "Returns true iff args is the correct length for f."
  (== (count (parameters f))
      (count args)))


(defn invoke* 
  ([f args]
   (invoke* f {} args))
  ([f env args]
    (let [f (value f env)]
      (if (zero? (count args))
        f
        (let [args* (if (map? args) args (zipmap (map name* (parameters f)) args))]
          (assert! (is-pog-fn? f (count args)))
          (if (implements? LazyPogFn f)
            (lazy-invoke f env args*)
            (invoke f (map/value-map args* #(value % env)))))))))

(defn partial* [f args-map]
  "Partially applies a Pog function using args-map, which
   maps applied parameters to their values."
  (reify
    ObjTypeStringable
    (obj-type-str [f] (fn->str f))
    LazyPogFn
    (lazy-invoke [_ env args]
      (invoke* f env (map (comp (merge args args-map)
                                name*)
                          (parameters f))))
    PogFn
    (parameters [_] (filter #(not (contains? args-map %))
                            (map name* (parameters f))))
    (invoke [f env]
      (lazy-invoke f env env))))

(defn value 
  "Returns (invoke v env) if v is a nullary PogFn,
   otherwise just v."
  ([v]
   (value v {}))
  ([v env]
  (cond (var*? v) (value (lazy-invoke v env {}) env)
        (is-pog-fn? v 0) (if (implements? LazyPogFn v)
                           (lazy-invoke v env {})
                           (invoke v {}))
        :else v)))

(defn fn-invoke [f parameters args]
  "Invokes clojure function f on args, reducing any nullary
   PogFn into its output."
  (apply f (for [param (map name* parameters)]
             (value (args param)))))

(defn fn->pog-fn 
  "Returns a PogFn wrapping around a Clojure function."
  ([f name parameters]
   (fn->pog-fn f name parameters ""))
  ([f name parameters docstring]
    (basic-pog-fn parameters docstring
                 (partial fn-invoke f parameters))))

(defn seq->partial-pog-fn [seq]
  "Converts seq into an applied PogFn. Unlike seq->pog-fn,
   all elements must be already PogFns."
  (if (seq? seq)
    (let [[f & args] seq
          f* (seq->partial-pog-fn f)
          args* (map seq->partial-pog-fn args)]
      (reify 
        ObjTypeStringable
        (obj-type-str [f] (fn->str f))
        LazyPogFn
        (lazy-invoke [f env args]
          (invoke* f* env args*))
        PogFn
        (parameters [_] [])
        (invoke [_ env]
          (invoke* f* env args*))))
    seq))

(defn invoke-seq 
  "Invokes the function represented by the seq, with the first argument
   as the function, followed by its arguments. Will recursively invoke
   any nested function-sequences."
  ([seq env args]
   (invoke* (seq->partial-pog-fn seq) 
            (merge env 
              (map/value-map args
                (fn [f]
                  (reify PogFn
                    (parameters [_] [])
                    (invoke [_ _] (value f env))))))
            {})))

(defn seq->pog-fn
  "Returns a PogFn that invokes the function represented by seq.
   See invoke-seq for details"
  ([name params seq]
   (seq->pog-fn name params "" seq))
  ([name params docstring seq]
   (seq->pog-fn name params docstring seq nil))
  ([name params docstring seq source]
   (reify
     ObjTypeStringable
     (obj-type-str [f] (fn->str f))
     PogFn
     (parameters [_] params)
     (docstring [_] docstring)
     Sourceable
     (source-code [f] source)
     LazyPogFn
     (lazy-invoke [f env args]
        (invoke-seq seq (assoc env name f) args)))))


(def if* 
 (reify 
   ObjTypeStringable
   (obj-type-str [f] (fn->str f))
   LazyPogFn
   (lazy-invoke [f env {pred "predicate" true-fn "true-fn" false-fn "false-fn"
                        :as args}]
      (let [pred (value pred env)]
        (assert! (implements? Boolean pred))
        (if pred
          (value true-fn env)
          (value false-fn env))))
   PogFn
   (parameters [_]  [{:name "predicate"
                      :type (PogFnType. 0)}
                        "true-fn" "false-fn"])
   (docstring [_] 
              (docstr [["pred" "a boolean"]
                       ["true-fn" "a value"]
                       ["false-fn" "a value"]]
                      "true-fn if pred is true, else it returns false-fn."))
   (invoke [f env]
      (lazy-invoke f env env))))

(def let* 
  (reify
    ObjTypeStringable
    (obj-type-str [f] (fn->str f))
    LazyPogFn
    (lazy-invoke [_ env args]
      (value (env "function") (assoc env (:name (args "var"))
                                         (args "val"))))
    PogFn
   (parameters [_] [{:name "var"
                     :type PogFnVar}
                     "val" "function"])
   (docstring [_]  
     (docstr [["var" "a variable"]
              ["val" "a value"]
              ["function" "a function to execute"]]
             "function executed with variable set to value"))
   (invoke [f env]
      (lazy-invoke f env env))))

(defn constantly* [obj]
  (fn->pog-fn (constantly obj) "constant"  []))

(defn comp* [f1 f2]
  "Composes two PogFunctions."
  (seq->pog-fn "compose" ["f" "g" "x"] (list (var* "f") 
                                             (list (var* "g") (var* "x")))))
