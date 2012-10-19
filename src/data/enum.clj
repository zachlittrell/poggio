(ns data.enum
  (:use [data.string :only [split-camel-case]]
        [clojure.string :only [lower-case]]))

(defmacro enum->keyword-map
  "Returns a map where the values are 
   the possible values of enum and the keys
   are the respective values, converted
   from camel-case to dashed-case.
   See split-camel-case for possible issues."
  [enum]
  `(into {} (for [value# (. ~enum (values))]
              [(->> (str value#)
                    (split-camel-case)
                    (map lower-case)
                    (interpose "-")
                    (apply str)
                    (keyword))
               value#])))

(defmacro def-enum-keyword-map
  "Defines a map called name whose keys and values
   are the same as the ones returned by 
   (enum->keyword-map enum)."
  [name enum]
  `(def ~name (enum->keyword-map ~enum)))
