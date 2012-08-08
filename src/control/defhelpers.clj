(ns control.defhelpers
  (:use [control.io :only [err-println]]))

(defn keyword->symbol
  "Converts a keyword to a symbol."
  [keyword]
  (symbol (name keyword)))

(defmacro def-opts-constructor
  "Defines a function called name that takes (currently) only keyword
   arguments, using all default values for keys as defined in 
   defaults, calling constructor (with defaults defined in the bindings)
   and for each option given, the instance created by
   constructor is passed, along with the option, to the 
   handler in handlers with the matching key."
  [name defaults constructor handlers]
  (let [{:as defaults-map} defaults
        default-symbols (map keyword->symbol (keys defaults-map))]
    `(let [handlers# ~handlers]
       (defn ~name [~'& options#]
         (let [{:keys [~@default-symbols]
                :as opts-map#
                :or ~defaults-map} options#
               instance# ~constructor]
           (doseq [[key# val#] opts-map#]
             (if-let [handle# (handlers# key#)]
               (handle# instance# val#)
               (err-println key# " is not a valid option.")))
           instance#)))))

