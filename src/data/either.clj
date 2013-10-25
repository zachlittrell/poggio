(ns data.either
  "Namespace for Either objects. If an Either object is 'right', then the 
   value is 'correct,' in whatever context it is being used. Vice versa,
   'left' Either objects are 'incorrect.'")

(defn right [val]
  {:value val
   :right? true})

(defn left [val]
  {:value val
   :right? false})

(defn right? [either]
  (:right? either))

(defn left? [either]
  (not (right? either)))

(defn try-right
  "Returns the result of computing f in a Right object,
   or the resulting exception in a Left object.
   Note, it also captures StackOverflowErrors."
  [f]
  (try
    (right (f))
    (catch StackOverflowError e
      (left (Exception. "Too many functions were called. (Possibly due to an infinite loop).")))
    (catch Exception e
      (left e))))

(defmacro on-right
  [& body]
  `(try-right (fn [] ~@body)))

(defn on-either
  "Returns the result of applying the value of either to right-fn
   if either is right, else applying the value of either to left-fn."
  [either right-fn left-fn]
  (if (right? either)
    (right-fn (:value either))
    (left-fn (:value either))))

