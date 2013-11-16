(ns poggio.functions.utilities
  (:require [clojure.string :as str])
  (:use [control assert]))

(defn docstr [param-desc-pairs returned]
  (if (empty? param-desc-pairs)
    (str "Returns: " returned)
    (format "Takes: %s.\nReturns: %s."
      (str/join ", "
                (for [[param desc] param-desc-pairs]
                  (str desc " called " param)))
      returned)))


