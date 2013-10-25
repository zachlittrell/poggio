(ns poggio.functions.parser
  (:require [instaparse.core :as insta])
  (:use [data either]
        [poggio.functions core]))

(def var-pattern "[^\\[\\]\\(\\).\\s0-9-][^\\[\\]\\(\\).\\s]*")

(defn is-var-name? [var-name]
  (re-matches (re-pattern var-pattern) var-name))

(def code-cfg
  (format "<EXPR> = <OWS> VAR <OWS>
                  | <OWS> NUM <OWS>
                  | (<OWS> LAMBDA <OWS>
                  / <OWS> PGROUP <OWS>)
                  | <OWS> VEC <OWS>
           BINDING = <LB><OWS>(VAR <OWS>)*<RB>
           LAMBDA = <LP><OWS><#'function'><OWS>BINDING<OWS>EXPR<RP>
           PGROUP = <LP> EXPR+ <RP>
           VEC = <LB> EXPR* <RB> | <LB> <OWS> <RB>
           NUM = #'-?[0-9]+(\\.[0-9]+)?'
           VAR = #'%s'
           WS = #'\\s+'
           OWS = #'\\s*' 
           LB = #'\\['
           RB = #'\\]'
           LP = #'\\('
           RP = #'\\)'"
          var-pattern))

(def parser (insta/parser code-cfg))

(defn parse [code]
  (let [parse-tree (parser code)]
    (if (insta/failure? parse-tree)
      (left (:line parse-tree))
      ;;TODO prevent PGROUP from grabbing lambdas
      (on-right 
        (insta/transform {:VAR (fn [arg] 
                                 (if (= arg "function")
                                   (throw (Exception. "Variables cannot be named function.\n(Possibly you entered a function incorrectly?)"))
                                   (var* arg)))
                          :NUM bigdec
                          :PGROUP (fn [& args] 
                                      args)
                          :BINDING (fn [& args] args)
                          :LAMBDA (fn [binding expr]
                                    (list lambda* (map->BindingWrapper
                                                    {:bindings binding})
                                          expr))
                          :VEC (fn [& args]
                                 (reduce (fn [result next]
                                           (list (var* "cons") next result))
                                         (var* "nil") (reverse args)))}
                         parse-tree)))))

(defn code-pog-fn [parameters docstring code]
  (let [code-seq? (parse code)]
    (on-either code-seq?
      (fn [code-seq]
        (seq->pog-fn "" parameters docstring
            code-seq
            code))
      (fn [failure]
        (throw (Exception. (if (instance? Exception failure)
                             (.getMessage failure)
                             (str failure))))))))
