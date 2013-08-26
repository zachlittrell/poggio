(ns poggio.functions.modules
  (:use [poggio.functions core list number color value])
  (:require [clojure.string :as str]))

(defn path [module name]
  "Encodes the module and name into a single string path"
  (str module " " name))

(defn path-components [path]
  "Decodes the path into a pair, the first element being
   module, and second element being the function name."
  (str/split path #" "))

(defn look-up-path [fn-map path]
  "Returns the function represented by the path in fn-map,
   if it exists. Else, returns nil."
  (get-in fn-map (path-components path)))

(def core-modules  
  {"Logic" {"if" if*}
   "List" {"cons" cons*
           "nil"  empty-list*
           "tail" tail*
           "head" head*
           "empty?" empty?*
           "repeat" repeat*
           "triple" triple*
           "reduce" reduce*
           "flatten" flatten*
           "map" map*}
   "Color" {"red" red*
            "green" green*
            "blue" blue*
            "color" color*
            "r-value" red-value*
            "g-value" green-value*
            "b-value" blue-value*}
   "Value" {"id" id*}
   "User" {}})

(defn is-core-fn? [module name]
  (get-in core-modules [module name]))
