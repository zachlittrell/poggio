(ns data.coll
  (:require [clojure.math.combinatorics :as combo]
            [clojure.zip :as zip]))

(defn distinct-by [f coll]
  "Returns coll with only distinct elements, where two elements are 
   equal if they have the same output from f."
  (map first (vals (group-by f coll))))

(defmacro >|_| [obj & forms]
  "Returns a vector starting with obj, followed by the result of
   computing each form with obj inserted after the first item."
  (let [obj* (gensym "obj")
        forms* (for [[head & form*] forms] (list* head obj* form*))]
  `(let [~obj* ~obj]
     [~obj* ~@forms*])))

(defmacro >>|_| [obj & forms]
  "Returns a vector starting with obj, followed by the result of
   computing each form with obj appended."
  (let [obj* (gensym "obj")
        forms* (for [form forms] (concat form [obj*]))]
    `(let [~obj* ~obj]
       [~obj* ~@forms*])))

(defmacro |_|< [obj & forms]
  "Returns a vector starting with obj, preceded by the result of
   computing each form with obj inserted after the first item."
  (let [obj* (gensym "obj")
        forms* (for [[head & form*] forms] (list* head obj* form*))]
  `(let [~obj* ~obj]
     [~@forms* ~obj*])))

(defmacro |_|<< [obj & forms]
  "Returns a vector starting with obj, preceded by the result of
   computing each form with obj appended."
  (let [obj* (gensym "obj")
        forms* (for [form forms] (concat form [obj*]))]
    `(let [~obj* ~obj]
       [~@forms* ~obj*])))

(defn zip [& colls]
  "Returns a list with each coll, in order, 'zipped' into vectors with
   the corresponding members sharing a vector."
  (apply map vector colls))

(defn zip-with [f & colls]
  "Returns the result of applying f to 
  the corresponding members in each coll."
  (apply map f colls))

(defn perm-match [preds coll] 
  "Returns the first permutation of coll such that applying
   each corresponding predicate in preds with an element in 
   the permutation returns true, or nil if no such permutation
   exists."
  (some #(when (every? identity (for [[pred x] (zip preds %)]
                                  (pred x)))
           %)
          (combo/permutations coll)))

(defn perm-some [preds coll]
  "Like perm-match, except returns the result of applying the predicates to
   the matching permutation."
  (some #(let [perm* (for [[pred x] (zip preds %)] (pred x))]
           (when (every? identity perm*)
             perm*))
        (combo/permutations coll)))


(defn seq-walk [leaf branch seq]
  "Returns the result of applying leaf to all leaf elements,
   and branch to all subsequences."
  (loop [zipper (zip/seq-zip seq)]
    (if (zip/end? zipper)
      (zip/root zipper)
      (recur (zip/next (zip/edit zipper (if (zip/branch? zipper)
                                        branch
                                        leaf)))))))


(defn nested-leaf-map [f xs]
  "Maps f onto each element in xs, or in subsequences."
  (seq-walk f identity xs))

(defn nested-branch-map [f xs]
  "Maps f onto each subsequence in xs."
  (seq-walk identity f xs))
