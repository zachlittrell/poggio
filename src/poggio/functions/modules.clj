(ns poggio.functions.modules
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

