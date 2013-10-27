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
               "(cons o (repeat o))"))

(def empty?* (fn->pog-fn empty? "empty?" ["xs"]
                (docstr [["xs" "a list"]] 
                        "true if xs is empty")))

(def single* (code-pog-fn ["a"]
                  (docstr [["a" "a value"]]
                          "a list whose only element is a")
                  "(cons a nil)"))

(def pair* (code-pog-fn ["a" "b"]
                (docstr [["a" "a value"] ["b" "a value"]]
                  "a list with the elements a and b")
                "(cons a (cons b nil))"))

(def triple* (code-pog-fn ["a" "b" "c"]
                (docstr [["a" "a value"] ["b" "a value"] ["c" "a value"]]
                        "a list with the elements a, b, and c")
                "(cons a (cons b (cons c nil)))"))

;;TODO maybe include a left fold 
(def reduce* (code-pog-fn ["f" "init" "xs"]
                (docstr [["f" "a binary function"]
                         ["init" "an initial value"]
                         ["xs" "a list"]]
                  "the result of f (head xs) (reduce f init (tail xs)). Think of it as folding xs up from the right side.")
"(if (empty? xs)
   init
   (f (head xs) (reduce f init (tail xs))))"))

(def concat* (code-pog-fn ["xs" "ys"]
                (docstr [["xs" "a list"] ["ys" "a list"]]
                        "a list of xs's elements, followed by ys's elements")
"(if (empty? xs)
  ys
  (cons (head xs) (concat (tail xs) ys)))"))

(def flatten* (code-pog-fn ["xs"]
                (docstr [["xs" "a list of lists"]]
                        "a list of xs's lists joined together")
                "(reduce concat [] xs)"))

(def map* (code-pog-fn ["f" "xs"]
            (docstr [["f" "a unary function"]
                     ["xs" "a list"]]
             "a list with f applied to each element in xs")
"(if (empty? xs)
   nil
   (cons (f (head xs)) (map f (tail xs))))"))

(def filter* (code-pog-fn ["f" "xs"]
            (docstr [["f" "a unary function"]
                     ["xs" "a list"]]
              "a list with only elements s.t. (f element) returns true")
"(if (empty? xs)
  nil
  (if (f (head xs))
    (cons (head xs)
          (filter f (tail xs)))
    (filter f (tail xs))))"))

(def get* (code-pog-fn ["i" "xs"]
              (docstr [["i" "a non-negative number"]
                       ["xs" "a list"]]
                "the ith element of xs. The first element is at index 0.")
             "(head (drop i xs))"))

(def size* (code-pog-fn ["xs"]
              (docstr [["xs" "a list"]]
                "the number of elements in xs.")
             "(if (empty? xs) 0 (inc (size (tail xs))))"))

(def take* (code-pog-fn ["n" "xs"]
              (docstr [["n" "a non-negative number"]
                       ["xs" "a list"]]
                "a list of the first n items of xs, or all of xs if there aren't n items")
"(if (or (less-than? n 1) (empty? xs))
   nil
   (cons (head xs) (take (dec n) (tail xs))))"))

(def drop* (code-pog-fn ["n" "xs"]
              (docstr [["n" "a non-negative number"]
                       ["xs" "a list"]]
                "xs without the first n items.")
"(if (or (less-than? n 1) (empty? xs))
   xs
   (drop (dec n) (tail xs)))"))

(def cycle* (code-pog-fn ["xs"]
              (docstr [["xs" "a list"]]
                "returns the elements of xs repeated over and over again."))
  "(flatten (repeat xs))")
