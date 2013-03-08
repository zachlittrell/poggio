(ns control.bindings)

(defmacro let-weave [pred true-bindings false-bindings & body]
  "Creates a let binding which uses the bindings in true-bindings when
   pred is true and false-bindings otherwise. The body is composed of pairs,
   a vector of symbols and a form, separated by :>>, such that the form
   is inserted into the let if all of the symbols in the vector are 
   contained in the selected bindings. There can be a default code at the
   end with no binding."
  (let [in-true-bindings? (set (flatten (map first 
                                             (partition 2 true-bindings))))
        in-false-bindings? (set (flatten (map first 
                                              (partition 2 false-bindings))))
        not-delimiter? (partial not= :>>)
        body* (loop [body body
                     body* []]
                (if (empty? body)
                  body*
                  (let [[[before & _] 
                         [_ after & more :as right]] (split-with 
                                                        not-delimiter?
                                                        body)]
                    (if (empty? right)
                      (conj body* [[] (if (seq? body)
                                        (list* 'do body)
                                        body)])
                      (recur more (conj body* [before after]))))))
        true-body (for [[keys form] body*
                        :when (every? in-true-bindings? keys)]
                      form)
        false-body (for [[keys form] body*
                         :when (every? in-false-bindings? keys)]
                       form)]
    `(if ~pred
       (let ~true-bindings
         ~@true-body)
       (let ~false-bindings
         ~@false-body))))
