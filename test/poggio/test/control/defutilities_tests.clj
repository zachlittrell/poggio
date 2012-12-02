(ns poggio.test.control.defutilities-tests
  (:use [clojure.test]
        [control defutilities]))

(def always-true (atom true))
(def-opts-constructor foo
  [:simple]
  {:foo `(swap! always-true (constantly false))}
  nil
  {})
(deftest defaults-side-effects-test
  (is @always-true))
