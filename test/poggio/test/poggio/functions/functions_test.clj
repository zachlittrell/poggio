(ns poggio.test.poggio.functions.functions-test
  (:use [clojure.test]
        [poggio.functions core scenegraph number value list]))

;;(deftest var-test
;;  (is (= (value (var* "foo") {"foo" (constantly* 3)})
;;         3)))
;;
;;(deftest let-test
;;  (is (value (invoke* let* {} [(var* "foo") (constantly* 3) (var* "foo")]) {})
;;      3))
;;
;;(deftest cons-test
;;  (is (= (invoke* cons* {} [(constantly* 3) (constantly* (list))])
;;         `(3))))
(deftest seq-test
    (is (= (invoke* inc* {} [(constantly* 3)])
           4)))

(deftest repeat-test
  (is (= (take 3 (invoke* repeat* {} [(constantly* 3)]))
         '(3 3 3))))

(deftest map-test
  (is (= (invoke* map* {} [inc* (constantly* (list 1 2 3))])
         '(2 3 4))))

(deftest partial-test
  (is (= (invoke* (partial* inc* {"n" (constantly* 3)}) []) 4))
  (is (= (invoke* (partial* inc* {}) [(constantly* 3)]) 4)))

(deftest pog-fn-node-test
  (let [pfn (pog-fn-node :pog-fn inc*)
        pfn2 (pog-fn-node :pog-fn add*)]
    (is (= (invoke* pfn [(constantly* 3)])
           4))
    (is 
        (= (invoke* (partial* pfn {}) [(constantly* 3)])
           4))
    (is 
        (= (invoke* (partial* pfn {"n" (constantly* 3)}) [])
           4))
    (is (= (invoke* pfn2 [(constantly* 1) (constantly* 2)])
           3))
    (is (= (invoke* (partial* pfn2 {"n1" (constantly* 1)})
                    [(constantly* 2)])
           3))
    (is (= (invoke* (partial* (partial* pfn2 {"n1" (constantly* 1)})
                              {"n2" (constantly* 2)})
                    [])
           3))
    (is (= (first (invoke* repeat* [(invoke* triple* [1 2 3])]))
           '(1 2 3)))
    (is (= (invoke* flatten* ['((1) (2) (3))])
           '(1 2 3)))
    (is (= (take 3 (invoke* flatten* [(invoke* repeat* ['(3)])]))
           '(3 3 3)))

    (is (= (second (invoke* map* [inc* (repeat 3)]))
           4))

    (is (= (first  (invoke* concat* [(repeat 3) (repeat 3)]))
           3))
    
    ))
