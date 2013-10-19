(ns poggio.functions.modules
  (:use [poggio.functions boolean core list music number color value])
  (:require [clojure.string :as str]))

(defn path
  "Encodes the module and name into a single string path"
  [module name]
  (str module " " name))

(defn path-components
  "Decodes the path into a pair, the first element being
   module, and second element being the function name."
  [path]
  (str/split path #" "))

(defn look-up-path
  "Returns the function represented by the path in fn-map,
   if it exists. Else, returns nil."
  [fn-map path]
  (get-in fn-map (path-components path)))

(def core-modules  
  {"Logic" {"if" if*
            "true" true*
            "false" false*
            "not" not*}
   "List" {"cons" cons*
           "concat" concat*
           "nil"  empty-list*
           "tail" tail*
           "head" head*
           "empty?" empty?*
           "repeat" repeat*
           "triple" triple*
           "reduce" reduce*
           "flatten" flatten*
           "map" map*
           "single" single*
           "pair" pair*}
   "Music" {"note" note*
            "pitch" pitch*
            "duration" duration*
            "rest" rest*
            "rest?" rest?*
            "c" c*
            "d" d*
            "e" e*
            "f" f*
            "g" g*
            "a" a*
            "b" b*
            "high-c"   high-c*}
   "Number" {"add" add*
             "subtract" subtract*
             "multiply" multiply*
             "divide" divide*
             "less-than?" less-than?*
             "greater-than?" greater-than?*
             "equal?" equal?*}
   "Color" {"red" red*
            "green" green*
            "blue" blue*
            "color" color*
            "r-value" red-value*
            "g-value" green-value*
            "b-value" blue-value*}
   "Functions" {"function" lambda*}
   "Value" {"id" id*
            "let" let*}
   "User" {}})

(def core-env
  (into {}
    (for [[module fs] core-modules
          f fs]
      f))) 

(reset! internal-core-env core-env)

(defn is-core-fn? [module name]
  (get-in core-modules [module name]))
