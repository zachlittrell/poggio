(ns poggio.test.data.coll-test
  (:use [clojure.test]
        [data coll]))

(deftest >|_|-test
  (is (= (>|_| 3 (- 1) (- 2) (- 3))
         [3 2 1 0]))
  (is (= (>>|_| 3 (- 1) (- 2) (- 3))
         [3 -2 -1 0]))
  (is (= (|_|< 3 (- 1) (- 2) (- 3))
         [2 1 0 3]))
  (is (= (|_|<< 3 (- 1) (- 2) (- 3))
         [-2 -1 0 3])))

