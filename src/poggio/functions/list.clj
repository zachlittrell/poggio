(ns poggio.functions.list
  (:use [control assert]
        [data object]
        [poggio.functions core parser value utilities]))

(def cons*
  (reify
    ObjTypeStringable
    (obj-type-str [f] (fn->str f))
    PogFn
    (parameters [_] ["head" "tail"])
    (docstring [_] (docstr [["head" "a value"] ["tail" "a list"]]
                      "a list whose first element is head and last elements are tail"))
    (invoke [f env]
      (lazy-invoke f env env))
    LazyPogFn
     (lazy-invoke [f env {head "head" tail "tail" :as arg}]
        (cons (value head env)
              (lazy-seq 
                (let [tail' (value tail env)]
                  (assert! (implements? clojure.lang.Seqable tail'))
                  tail'))))))

(def head* (fn->pog-fn (fn [xs]
                         (assert! (not-empty xs))
                         (first xs)) "head" 
                         [{:name "xs"
                           :type clojure.lang.Seqable}]
                       (docstr [["xs" "a list"]]
                       "the first element of xs")))

(def tail* (fn->pog-fn (fn [xs]
                         (assert! (not-empty xs))
                         (rest xs)) "tail"
                       [{:name "xs"
                         :type clojure.lang.Seqable}]
                       (docstr [["xs" "a list"]]
                       "the last elements of xs")))

(def empty-list* (constantly* (list)))

(def repeat* (code-pog-fn ["o"]
               (docstr [["o" "a value"]]
                       "a list with the element o, over and over again.")
               "cons o (repeat o)"))

(def empty?* (fn->pog-fn empty? "empty?" ["xs"]
                (docstr [["xs" "a list"]] 
                        "true if xs is empty")))

(def triple* (code-pog-fn ["a" "b" "c"]
                (docstr [["a" "a value"] ["b" "a value"] ["c" "a value"]]
                        "a list with the elements a, b, and c")
                "cons a (cons b (cons c nil))"))

(def reduce* (code-pog-fn ["f" "xs" "init"]
                (docstr [["f" "a binary function"]
                         ["xs" "a list"]
                         ["init" "an initial value"]]
                  "the result of f (head xs) (reduce f (tail xs) init)")
"if (empty? xs)
  init
  (f (head xs) (reduce f (tail xs) init))"))

(def concat* (code-pog-fn ["xs" "ys"]
                (docstr [["xs" "a list"] ["ys" "a list"]]
                        "a list of xs's elements, followed by ys's elements")
"if (empty? xs)
  ys
 (cons (head xs) (concat (tail xs) ys))"))

(def flatten* (code-pog-fn ["xs"]
                (docstr [["xs" "a list of lists"]]
                        "a list of xs's lists joined together")
                "reduce concat xs nil"))

(def map* (code-pog-fn ["f" "xs"]
            (docstr [["f" "a unary function"]
                     ["xs" "a list"]]
             "a list with f applied to each element in xs")
"if (empty? xs)
  nil
  (cons (f (head xs)) (map f (tail xs)))"))
