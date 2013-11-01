(ns data.maybe
  "Functions that operate with a Maybe/Option type.")

(defprotocol Maybe)

(defrecord Just [just]
  Maybe)

(defn just [just]
  (Just. just))

(def nothing (reify Maybe))

(defn nothing? [m]
  (identical? m nothing))

(defn just?
  "Returns true iff m is not nothing."
  [m]
  (not (nothing? m)))

(defmacro if-just-let
  [[var maybe] just-form nothing-form]
  `(let [temp# ~maybe]
     (if (nothing? temp#)
       ~nothing-form
       (let [~var (:just temp#)]
         ~just-form))))
