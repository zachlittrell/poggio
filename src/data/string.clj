(ns data.string
  "Functions for manipulating strings."
  (:import [java.io StringWriter])
  (:require [clojure.string :as str]
            [clojure.pprint :as pretty]))

(defn upper-case-word [^String word]
  "Returns the word with the first letter
   capitalized."
  (if (zero? (.length word))
    ""
    (str (Character/toUpperCase (.charAt word 0))
         (subs word 1))))

(defn lower-case-word [^String word]
  "Returns the word with the first letter
   lowercase."
  (if (zero? (.length word))
    ""
    (str (Character/toLowerCase (.charAt word 0))
         (subs word 1))))

(defn dash->camel-case
  "Takes a string with each word
   separated by dashes and returns 
   the words combined in camel case.
   Optional argument upper? determines
   whether to use upper or lower camel case.
   By default, it uses lower-camel case."
  ([s]
   (dash->camel-case s false))
  ([^String s upper?]
   (let [[word & words] (seq (.split s "-"))]
     (apply str (if upper? (upper-case-word word)
                           (lower-case-word word))
                (map upper-case-word words)))))

(defn split-camel-case
  "Takes a word in camel-case (either upper or lower)
   and returns it split into a vector. Note,
   this expects it to be true camel-case and cannot handle
   certain cases like HTMLEditorKit properly."
  [s]
  (str/split s #"(?<!^)(?=[A-Z])")) 

(defn object->pretty-printed-string
  "Returns the object pretty-printed onto a string."
  [o]
  (let [writer (StringWriter.)]
    (pretty/pprint o writer)
    (.toString writer)))

(defn genstr
  "Returns a randomly generated string, using the counter
   from gensym."
  [prefix]
  (str prefix (clojure.lang.RT/nextID)))

(defn trivialize-whitespace
  "Returns the string s with single newlines turned into spaces,
   and multiple entries of whitespace turned into a single space."
  [s]
  (-> s
     (str/replace #"\s+" " ")))

