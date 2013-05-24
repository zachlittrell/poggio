(ns poggio.functions.list
  (:use [control assert]
        [data object]
        [poggio.functions core value utilities]))

(def cons* (basic-pog-fn ["head" "tail"]
              (docstr [["head" "a value"] ["tail" "a list"]]
                      "a list whose first element is head and last elements are tail")
              (fn [[head tail]]
                (cons (value head)
                      (lazy-seq 
                        (let [tail' (value tail)]
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
               (list cons* "o" (list "repeat" "o"))))

(def empty?* (fn->pog-fn empty? "empty?" ["xs"]))

(def triple* (seq->pog-fn "triple" ["a" "b" "c"]
                (docstr [["a" "a value"] ["b" "a value"] ["c" "a value"]]
                        "a list with the elements a, b, and c")
                (list cons* "a"
                      (list cons* "b"
                            (list cons* "c" empty-list*)))))

(def reduce* (seq->pog-fn "reduce" ["f" "xs" "init"]
                (list if* (list empty?* "xs")
                      "init"
                      (list "f" (list head* "xs")
                                (list "reduce" "f"
                                               (list tail* "xs")
                                               "init")))))


(def concat* (seq->pog-fn "concat" ["xs" "ys"]
                (docstr [["xs" "a list"] ["ys" "a list"]]
                        "a list of xs's elements, followed by ys's elements")
                (list if* (list empty?* "xs")
                      "ys"
                      (list cons* (list head* "xs")
                                  (list "concat" (list tail* "xs")
                                                 "ys")))))

(def flatten* (seq->pog-fn "flatten" ["xs"]
                (docstr [["xs" "a list of lists"]]
                        "a list of xs's lists joined together")
                (list reduce* concat* "xs" empty-list*)))

(def map* (seq->pog-fn "map" ["f" "xs"]
            (list if* (list empty?* "xs")
                  empty-list*
                  (list cons* (list "f" (list head* "xs"))
                              (list "map" "f" (list tail* "xs"))))))
