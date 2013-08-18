(ns poggio.functions.parser
  (:require [instaparse.core :as insta])
  (:use [data either]
        [poggio.functions core]))

(def var-pattern "[^\\(\\).\\s0-9][^\\(\\).\\s]*")

(def code-cfg
  (format "<EXPR> = <OWS> VAR <OWS>
                  | <OWS> NUM <OWS>
                  | <OWS> PGROUP <OWS>
                  | <OWS> CHAIN <OWS>
           PGROUP = <LP> EXPR <RP>
           <CHAIN> = EXPR EXPR+
           NUM = #'[0-9]+(\\.[0-9]+)?'
           VAR = #'%s'
           WS = #'\\s+'
           OWS = WS | EPSILON
           LP = #'\\('
           RP = #'\\)'"
          var-pattern))

(defn parse [code]
  (let [parser (insta/parser code-cfg)
        parse-tree (parser code)]
    (if (insta/failure? parse-tree)
      (left (:line parse-tree))
      (right 
        (insta/transform {:VAR var*
                          :NUM bigdec
                          :PGROUP (fn [& args] args)}
                         parse-tree)))))

(defn code-pog-fn [parameters docstring code]
  (let [code-seq? (parse code)]
    (on-either code-seq?
      (fn [code-seq]
        (seq->pog-fn "" parameters docstring
            code-seq))
      (fn [failure]
        (throw (Exception. failure))))))

