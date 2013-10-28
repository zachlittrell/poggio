(ns data.coll
  (:require [clojure.math.combinatorics :as combo]
            [clojure.zip :as zip]))

(defprotocol Adjoinable
  "Protocol for objects which can be 'adjoin'.
   Similar to IPersistentCollection with less requirements."
  (adjoin [adjoinable obj]))

(extend-protocol Adjoinable
  clojure.lang.IPersistentCollection
  (adjoin [coll obj] (cons coll obj)))


(defn distinct-by
  "Returns coll with only distinct elements, where two elements are 
   equal if they have the same output from f."
  [f coll]
  (map first (vals (group-by f coll))))

(defmacro >|_|
  "Returns a vector starting with obj, followed by the result of
   computing each form with obj inserted after the first item."
  [obj & forms]
  (let [obj* (gensym "obj")
        forms* (for [[head & form*] forms] (list* head obj* form*))]
  `(let [~obj* ~obj]
     [~obj* ~@forms*])))

(defmacro >>|_|
  "Returns a vector starting with obj, followed by the result of
   computing each form with obj appended."
  [obj & forms]
  (let [obj* (gensym "obj")
        forms* (for [form forms] (concat form [obj*]))]
    `(let [~obj* ~obj]
       [~obj* ~@forms*])))

(defmacro |_|<
  "Returns a vector starting with obj, preceded by the result of
   computing each form with obj inserted after the first item."
  [obj & forms]
  (let [obj* (gensym "obj")
        forms* (for [[head & form*] forms] (list* head obj* form*))]
  `(let [~obj* ~obj]
     [~@forms* ~obj*])))

(defmacro |_|<<
  "Returns a vector starting with obj, preceded by the result of
   computing each form with obj appended."
  [obj & forms]
  (let [obj* (gensym "obj")
        forms* (for [form forms] (concat form [obj*]))]
    `(let [~obj* ~obj]
       [~@forms* ~obj*])))

(defmacro |_|?
  "Returns a vector whose elements are from forms, with elements discarded
   if they are false."
  [& forms]
  (let [syms (map (comp gensym (constantly "sym")) forms)
        let-binding (interleave syms forms)]
    `(let [~@let-binding]
       (vec (filter identity [~@syms])))))

(defn zip
  "Returns a list with each coll, in order, 'zipped' into vectors with
   the corresponding members sharing a vector."
  [& colls]
  (apply map vector colls))

(defn zip-with
  "Returns the result of applying f to 
  the corresponding members in each coll."
  [f & colls]
  (apply map f colls))

(defn index
  "Returns a list mapping each element with its index.
   Example: (index [:a :b :c]) -> ([0 :a] [1 :b] [2 :c])"
  [xs]
  (zip (iterate inc 0) xs))

(defn perm-match
  "Returns the first permutation of coll such that applying
   each corresponding predicate in preds with an element in 
   the permutation returns true, or nil if no such permutation
   exists."
  [preds coll]
  (some #(when (every? identity (for [[pred x] (zip preds %)]
                                  (pred x)))
           %)
          (combo/permutations coll)))

(defn perm-some
  "Like perm-match, except returns the result of applying the predicates to
   the matching permutation."
  [preds coll]
  (some #(let [perm* (for [[pred x] (zip preds %)] (pred x))]
           (when (every? identity perm*)
             perm*))
        (combo/permutations coll)))

(defn random
  "Returns a random element of coll."
  [coll]
  (nth coll (rand-int (count coll))))


(defn seq-walk
  "Returns the result of applying leaf to all leaf elements,
   and branch to all subsequences."
  [leaf branch seq]
  (loop [zipper (zip/seq-zip seq)]
    (if (zip/end? zipper)
      (zip/root zipper)
      (recur (zip/next (zip/edit zipper (if (zip/branch? zipper)
                                        branch
                                        leaf)))))))


(defn nested-leaf-map
  "Maps f onto each element in xs, or in subsequences."
  [f xs]
  (seq-walk f identity xs))

(defn nested-branch-map
  "Maps f onto each subsequence in xs."
  [f xs]
  (seq-walk identity f xs))
