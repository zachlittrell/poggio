(ns data.keyword)

(defn keyword->symbol
  "Converts a keyword to a symbol."
  [keyword]
  (symbol (name keyword)))
