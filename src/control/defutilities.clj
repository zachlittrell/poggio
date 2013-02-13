(ns control.defutilities
  "Methods for helping construct def-* macros."
  (:require [clojure.string :as str])
  (:use [control.io :only [err-println]]
        [data [function :only [no-op]]
              [map :only [key-map intersecting-merge]]
              [string :only [dash->camel-case]]
              [keyword :only [keyword->symbol]]]
        [clojure.tools.macro :only [name-with-attributes]]))

(defmacro def-directed-opts-constructor-body*
  "Returns the body of the macro specified in
   def-directed-opts-constructor-body, which
   creates an instance of the object made by
   const, and checks each directive in dir-map
   against defaults in defs and options given
   in opts. If there is no directive in dir-map
   for handling the option, the default-directive is used."
  [director default-directive dir-map defs const opts]
  (let [{:as opts-map} opts
        ;;We merge only the defaults that are in opts
        def-map (intersecting-merge defs opts-map)
        opts-map (merge defs opts-map)
        sym-map (key-map keyword->symbol def-map)
        flat-def-map (apply concat sym-map)
        instance-sym (gensym "constructor-instance-sym")
        opts-forms (for [[option argument] opts-map]
                     (if (contains? dir-map option)
                       (let [directive (dir-map option)]
                         (apply director
                                instance-sym
                                argument
                                option
                                (first directive)
                                (next directive)))
                       (apply director 
                              instance-sym
                              argument
                              option
                              (first default-directive)
                              (next default-directive))))]
    `(let [~@flat-def-map
           ~instance-sym ~const]
       ~@opts-forms
       ~instance-sym)))

(defmacro def-directed-opts-constructor-body
  "Returns the body of the macro specified in
   def-directed-opts-constructor, which creates
   a macro called name that takes a variable number
   of keyword arguments, handled as per the director,
   default directive, and given directives."
  [director name default-directive defaults constructor directives]
  `(let [dir-map# ~directives
         defs#    ~defaults]
     (defmacro ~name [~'& opts#]
       `(def-directed-opts-constructor-body* ~~director
                                             ~~default-directive
                                             ~dir-map#
                                             ~defs#
                                             ~~constructor
                                             ~opts#))))


(defmacro def-directed-opts-constructor
  "Creates a macro called name that takes a
   name, a default-directive for director,
   pairs of keys with default values (with the values 
   quoted as necessary), a constructor (once again quoted),
   and pairs of directives. The directives are handled
   according to the director."
  [name director]
  `(let [director# ~director]
     (defmacro ~name [name# default-directive# defaults# constructor# directives#]
       `(def-directed-opts-constructor-body ~director# 
                                            ~name#
                                            ~default-directive#
                                            ~defaults#
                                            ~constructor#
                                            ~directives#))))


;;Setup for the specialized def-opts-constructor
(def default-argument-modifiers
  "The default modifiers on arguments given to the 
   default director. Includes:
   thread-in -- inserts argument as second argument
                in the given form.
   append-in -- Similar to thread-in, except inserts
                argument at the end."
  {:thread-in (fn [option instance arg]
                  (list* (first arg) instance (next arg)))
   :append-in (fn [option instance arg]
                  (concat arg (list instance)))})

(def default-instance-modifiers 
  "The default modifiers on instance symbols given
   to the default director. Currently includes nothing."
  {})

(def default-fn-name-modifiers
  "The default modifiers on the fn-name given to
   the default director. Includes:
   replace -- Replaces the fn-name according to 
              the given pattern and replacement."
  {:replace (fn [option name [pattern replacement]]
                (str/replace name pattern replacement))}) 

(defn default-modifiers-handlers 
  "Returns a pair with the first element being the symbol of the fn-name modified as specified by args, prepended with ., and turned into
   camel-case, and instance and argument modified as specified
   by args."
  [fn-name instance argument & args]
  (let [{:as args-map} args]
    (loop [fn-name (name fn-name)
           instance instance
           argument argument
           args (seq args-map)]
      (if-let [[[keyword arg] & more] args]
        (if-let [f (default-instance-modifiers keyword)]
          (recur fn-name (f keyword instance arg) argument more)
          (if-let [f (default-fn-name-modifiers keyword)]
            (recur (f keyword fn-name arg) instance argument more)
            (if-let [f (default-argument-modifiers keyword)]
              (recur fn-name instance (f keyword argument arg) more)
              (err-println "No such argument called:" keyword))))
        [(symbol (str "." (dash->camel-case fn-name))) 
                 instance
                 argument]))))

(defn default-directive-handler
  "Returns a form that simply applies instance and argument to the function."
  ([instance argument name-keyword & args]
   (let [[fn-name instance argument] (apply default-modifiers-handlers 
                                            name-keyword
                                            instance
                                            argument
                                            args)]
     `(~fn-name ~instance ~argument))))

(defn setter-directive-handler
  "Returns a form similar to default-directive-handler,
   but prepended with 'set'"
  [instance argument name-keyword & args]
  (apply default-directive-handler
         instance
         argument
         (keyword (str "set-" (name name-keyword)))
         args))

(defn pure-directive-handler
  "Returns a form that applies instance and argument to the function,
   and assumes the function is a Clojure function."
  ([instance argument name-keyword & args]
   (let [[fn-name instance argument] (apply default-modifiers-handlers 
                                            name-keyword
                                            instance
                                            argument
                                            args)
         fn-name (keyword (str/replace (name fn-name) #"^\." ""))]
     `(~fn-name ~instance ~argument))))



(defn do-seq-directive-handler
  "Returns a form that loops over every element
   in argument and applies our function to the instance
   with the element"
  [instance argument name-keyword & args]
  (let [[fn-name instance argument] (apply default-modifiers-handlers
                                           name-keyword
                                           instance
                                           argument
                                           args)]
    `(doseq [variable# ~argument]
       (~fn-name ~instance variable#))))

(defn map-do-seq-directive-handler
  "Returns a form that loops over every key-value pair 
   in argument, and applies our function to the instance
   with the key and value."
  [instance argument name-keyword & args]
  (let [[fn-name instance argument] (apply default-modifiers-handlers
                                           name-keyword
                                           instance
                                           argument
                                           args)]
    `(doseq [[key# val#] ~argument]
       (~fn-name ~instance key# val#))))

(defn instance-only-directive-handler
   "Returns just the resulting symbol for the instance."
  [instance argument name-keyword & args]
  (let [[fn-name instance argument] (apply default-modifiers-handlers
                                           name-keyword
                                           instance
                                           argument
                                           args)]
     instance))

(defn arg-only-directive-handler
   "Returns just the resulting symbol for the argument."
  [instance argument name-keyword & args]
  (let [[fn-name instance argument] (apply default-modifiers-handlers
                                           name-keyword
                                           instance
                                           argument
                                           args)]
     argument))

(def default-director-directives-handlers
  "The directives that default-director knows how to handle.
   These include:
  
   simple -- :option arg => (.option instance arg)
   setter -- :option arg => (.setOption instance arg)
   pure   -- :option arg => (option instance arg)
   no-op  -- :option arg => nil
   do-seq -- :option arg => (doseq [val arg]
                              (.option instance val))
   map-do-seq -- :option arg => (doseq [[key val] arg]
                                  (.option instance key val))
   arg-only  -- :option arg => arg
   instance-only -- :option arg => instance"
  {:simple default-directive-handler
   :setter setter-directive-handler
   :pure   pure-directive-handler
   :no-op (constantly nil)
   :do-seq do-seq-directive-handler
   :map-do-seq map-do-seq-directive-handler
   :arg-only arg-only-directive-handler
   :instance-only instance-only-directive-handler})

(defn default-director
  "Returns a form that modifies the symbol instance
   with the given function name, possibly a directive
   (if no directive is given, :simple is used), and
   any additional args."
  ([instance argument name]
   (default-director instance argument name :simple))
  ([instance argument name directive & args]
   (apply (default-director-directives-handlers directive)
           instance 
           argument
           name
           args)))

(def-directed-opts-constructor def-opts-constructor 
                               default-director)
