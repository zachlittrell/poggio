(ns data.symbol
  "Functions for manipulating Clojure symbols."
  (:require [clojure.zip :as zip])
  (:import [clojure.lang Symbol]
           [java.util.regex Pattern]))

(defprotocol SymbolReplacementPattern
  (match? [this symbol]))

(extend-type String
  SymbolReplacementPattern
  (match? [this symbol](= (name symbol) this)))

(extend-type Pattern
  SymbolReplacementPattern
  (match? [this symbol] (-> (re-matcher this (name symbol))
                            (re-find))))

(extend Symbol
  SymbolReplacementPattern
  (:match? =))

(defn replace-all [body pattern replacement]
  (if (and (symbol? body) (match? pattern body))
    replacement
    (loop [zip (zip/seq-zip body)]
      (cond (zip/end? zip)    (zip/root zip)
            (zip/branch? zip) (recur (zip/next zip))
            :else             (let [node (zip/node zip)]
                                (recur (if (and (symbol? node)
                                                (match? pattern 
                                                        node))
                                         (-> zip
                                             (zip/replace replacement)
                                             (zip/next))
                                         (zip/next zip))))))))
        
