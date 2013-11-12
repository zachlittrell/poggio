(ns poggio.functions.value
  (:import [com.jme3.math ColorRGBA])
  (:use [control assert]
        [data object color]
        [poggio.functions core parser utilities]))

(def id* (code-pog-fn ["x"]
                (docstr [["x" "a value"]]
                        "x")
            "x"))

(defprotocol Equalizable
  (equal?** [o1 o2 env]))


(defn equal?*** [o1 o2 env]
  (assert! (implements? Equalizable o1))
  (equal?** o1 o2 env))

(extend-protocol Equalizable
  Number
  (equal?** [n1 n2 env]
    (assert! (implements? Number n2))
    (== n1 n2))
  ColorRGBA
  (equal?** [c1 c2 env]
    (assert! (implements? ColorRGBA c2))
    (rgb-equal? c1 c2))
  Boolean
  (equal?** [b1 b2 env]
    (assert! (implements? Boolean b2))
    (if b1 b2 (not b2)))
  clojure.lang.Seqable
  (equal?** [seq1 seq2 env]
    (assert! (implements? clojure.lang.Seqable seq2))
    (if (empty? seq1)
      (empty? seq2)
      (if (empty? seq2)
        false
        (let [head1 (value (first seq1) env)
              head2 (value (first seq2) env)]
          (if (equal?*** head1 head2 env)
            (recur (rest seq1) (rest seq2) env)
            false))))))
(defmethod type-str Equalizable [_]
  "comparable value")

(def equal?* 
  (reify 
    PogFn
    (parameters [_] ["x" "y"])
    (docstring [_] "")
    LazyPogFn
    (lazy-invoke [f env {x "x"
                         y "y"}]
      (equal?*** (value x env) (value y env) env))))
  
