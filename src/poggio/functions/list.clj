(ns poggio.functions.list
  (:use [control assert]
        [data object]
        [poggio.functions core value]))

(def cons* (basic-pog-fn ["head" "tail"]
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
                           :type clojure.lang.Seqable}]))

(def tail* (fn->pog-fn (fn [xs]
                         (assert! (not-empty xs))
                         (rest xs)) "tail"
                       [{:name "xs"
                         :type clojure.lang.Seqable}]))

(def empty-list* (constantly* (list)))

(def repeat* (seq->pog-fn "repeat" ["o"]
               (list cons* "o" (list "repeat" "o"))))

(def empty?* (fn->pog-fn empty? "empty?" ["xs"]))

(def triple* (seq->pog-fn "triple" ["a" "b" "c"]
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
                (list if* (list empty?* "xs")
                      "ys"
                      (list cons* (list head* "xs")
                                  (list "concat" (list tail* "xs")
                                                 "ys")))))

(def flatten* (seq->pog-fn "flatten" ["xs"]
                (list reduce* concat* "xs" empty-list*)))

(def map* (seq->pog-fn "map" ["f" "xs"]
            (list if* (list empty?* "xs")
                  empty-list*
                  (list cons* (list "f" (list head* "xs"))
                              (list "map" "f" (list tail* "xs"))))))
