(ns data.coll)

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

