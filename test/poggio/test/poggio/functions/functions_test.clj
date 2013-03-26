(ns poggio.test.poggio.functions.functions-test
  (:use [clojure.test]
        [poggio.functions core value list]))

(deftest seq->pog-fn-test2
  (let [triple* (seq->pog-fn "triple" ["a" "b" "c"] (list cons* "a"
                                                 (list cons* "b"
                                                       (list cons* "c"
                                                             empty-list*))))
        inc* (fn->pog-fn inc' "inc" ["n"])
        xs (invoke triple* [1 2 3])]
    (is (= (invoke map* [inc* (constantly* xs)])
           (list 2 3 4)))))

(deftest seq->pog-fn-test
  (let [triple* (seq->pog-fn "triple" ["a" "b" "c"]
                  (list cons* "a"
                        (list cons* "b"
                              (list cons* "c" empty-list*))))]
    (is (= (invoke triple* [1 2 3])
           (list 1 2 3)))))

