(ns functions.utilities
  "Utilities for interacting with functions.
   May be eventually moved into data.functions")

(defn no-op
  "Does nothing."
  ([])
  ([_])
  ([_ _])
  ([_ _ _])
  ([_ _ _ _])
  ([_ _ _ _ & _]))
