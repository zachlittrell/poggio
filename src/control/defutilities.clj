(ns control.defutilities
  (:use [control.io :only [err-println]]
        [string.utilities :only [dash->camel-case]]))

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

(defn keyword->setter-symbol 
  "Returns a symbol of the form set-keyword!"
  [keyword]
  (symbol (str "set-" (name keyword) "!"))) 

(defn predicate-keyword->setter-symbol
  "Returns a symbol of the form set-keyword!, with the
   last character trimmed off the end of the keyword."
  [keyword]
  (let [name (name keyword)]
    (symbol (str "set-" (.substring name 0 (dec (.length name))) "!"))))

(defn keyword->setter-defn-form
  "Returns a form for creating a defn of the form
   (defn name [instance val]
     (.keywordInCamelCase instance val))
   A function, f, can be optionally passed
  that will take the symbol for val and return a replacement
  form." 
  ([setter-name keyword]
    (let [fn-name (symbol (str "." (dash->camel-case (name keyword))))]
      `(defn ~setter-name [instance# val#]
         (~fn-name instance# val#))))
  ([f setter-name keyword]
   (let [fn-name (symbol (str "." (dash->camel-case (name keyword))))
         val-sym (gensym "val")
         val-sym* (f val-sym)]
     `(defn ~setter-name [instance# ~val-sym]
        (~fn-name instance# ~val-sym*)))))
     

(defn keyword->adder-symbol 
  "Returns a symbol of the form add-keyword!"
  [keyword]
  (symbol (str "add-" (name keyword) "!"))) 

(defn keyword->adder-defn-form
  "Returns a form for creating a defn of the form
   (defn name [instances val]
     (doseq [instance instances]
       (.keywordInCamelCase instance val)))
  A function, f, can be optionally passed
  that will take the symbol for val and return a replacement
  form."
  ([adder-name keyword]
    (let [name (name keyword)
          fn-name (symbol 
                   (str "." 
                     (-> name
                       (.substring 0 (dec (.length name)))
                       (dash->camel-case))))]
      `(defn ~adder-name [instance# val#]
         (doseq [addend# val#]
           (~fn-name instance# addend#)))))
  ([f adder-name keyword]
   (let [name (name keyword)
         fn-name (symbol 
                   (str "." 
                     (-> name
                       (.substring 0 (dec (.length name)))
                       (dash->camel-case))))
         val-sym (gensym "val")
         val-sym* (f val-sym)]
     `(defn ~adder-name [instance# ~val-sym]
        (doseq [addend# ~val-sym*]
          (~fn-name instance# addend#))))))

(def default-directives-map
  {:setter {:name keyword->setter-symbol
            :form keyword->setter-defn-form}
   :adder  {:name keyword->adder-symbol
            :form keyword->adder-defn-form}})

(defn default-director
  [key directive]
  (let [name ((:name directive) key)
        form ((:form directive) name key)]
    `(do 
       ~form
       (vector ~key ~name))))

(defmacro def-directive-map
  "Creates a macro called name that takes directives of the form
  [:key :val] and returns a map with :key mapped to the value
  as dictated by val in the directive map. May have a side effect."
  [name directive-map director]
  `(let [director# ~director
         directive-map# ~directive-map]
    (defmacro ~name [~'& directives#]
      (let [pairs# (for [[key# directive#] directives#]
                      (director# key# (directive-map# directive#)))]
        (list `into {} (cons `list pairs#))))))
