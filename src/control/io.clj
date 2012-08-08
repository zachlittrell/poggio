(ns control.io)

(defn err-println 
  "Prints line to *err*."
  [& more]
  (binding [*out* *err*]
    (apply println more)))

(defn err-print
  "Prints to *err*."
  [& more]
  (binding [*out* *err*]
    (apply print more)))
