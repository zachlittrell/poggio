(ns data.keyword
  "Functions for converting keywords
   into other data types.")

(defn keyword->symbol
  "Converts a keyword to a symbol."
  [keyword]
  (symbol (name keyword)))
