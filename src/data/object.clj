(ns data.object
  (:use [data.coll :only [distinct-by]])
  (:import [java.lang.reflect Method Modifier]))

(defmacro proxy-around-object [object ancestors args & body]
  "Creates a proxy extending/implementing ancestors and using args 
   for the constructor and body as the implementation for overrided
   methods. Any public or protected method of the ancestors not implemented
   by body is delegated to object.
   Currently does not handle objects with overloaded methods."
  (let [declared-method-impls (for [ancestor ancestors]
                                (let [ancestor* (resolve ancestor)]
                                  [(for [m (.getMethods ancestor*)]
                                     (let [method-name (.getName m)
                                           arg-name (name (gensym "arg"))
                                           args (for [i (range 0 (alength (.getParameterTypes m)))]
                                                  (symbol (str arg-name i)))
                                           method-sym (symbol (str method-name))
                                           call-sym (symbol (str "." method-name))]
                                       `(~method-sym [~@args] 
                                           (~call-sym ~object ~@args))))
                                    (for [m (.getDeclaredMethods ancestor*)]
                                     (let [method-name (.getName m)
                                           method-sym (symbol (str method-name))
                                           parameter-types (seq (.getParameterTypes m))
                                           arg-name (name (gensym "arg"))
                                           args (for [i (range 0 (alength (.getParameterTypes m)))]
                                                  (symbol (str arg-name i)))]
                                       `(~method-sym [~@args] 
                                           (let [m# (.getDeclaredMethod 
                                                     ~ancestor* 
                                                     ~method-name
                                                     (into-array Class
                                                            ~parameter-types))]
                                             (.setAccessible m# true)
                                             (.invoke m# ~object (into-array ~args))))))]))

        unquoted-body (for [[method-name & more] body]
                        (list* (symbol (name method-name)) more))
        impls (distinct-by first (apply concat unquoted-body (apply concat declared-method-impls)))]
    `(proxy ~ancestors ~args
      ~@impls)))
