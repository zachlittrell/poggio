(ns poggio.functions.list
  (:use [control assert]
        [data object]
        [poggio.functions core value utilities]))

(def cons*
  (reify
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

(def repeat* (seq->pog-fn "repeat" ["o"]
               (docstr [["o" "a value"]]
                       "a list with the element o, over and over again.")
               (list cons* (var* "o") (list (var* "repeat") (var* "o")))))

(def empty?* (fn->pog-fn empty? "empty?" ["xs"]))

(def triple* (seq->pog-fn "triple" ["a" "b" "c"]
                (docstr [["a" "a value"] ["b" "a value"] ["c" "a value"]]
                        "a list with the elements a, b, and c")
                (list cons* (var* "a")
                      (list cons* (var* "b")
                            (list cons* (var* "c") empty-list*)))))

(def reduce* (seq->pog-fn "reduce" ["f" "xs" "init"]
                (list if* (list empty?* (var* "xs"))
                      (var* "init")
                      (list (var* "f") (list head* (var* "xs"))
                                (list (var* "reduce") (var* "f")
                                               (list tail* (var* "xs"))
                                               (var* "init"))))))


(def concat* (seq->pog-fn "concat" ["xs" "ys"]
                (docstr [["xs" "a list"] ["ys" "a list"]]
                        "a list of xs's elements, followed by ys's elements")
                (list if* (list empty?* (var* "xs"))
                      (var* "ys")
                      (list cons* (list head* (var* "xs"))
                                  (list (var* "concat") 
                                        (list tail* (var* "xs"))
                                                 (var* "ys"))))))

(def flatten* (seq->pog-fn "flatten" ["xs"]
                (docstr [["xs" "a list of lists"]]
                        "a list of xs's lists joined together")
                (list reduce* concat* (var* "xs") empty-list*)))

(def map* (seq->pog-fn "map" ["f" "xs"]
            (list if* (list empty?* (var* "xs"))
                  empty-list*
                  (list cons* (list (var* "f") (list head* (var* "xs")))
                              (list (var* "map") (var* "f") (list tail* (var* "xs")))))))
