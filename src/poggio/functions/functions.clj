(ns poggio.functions.functions
  (:use [poggio.functions core parser utilities]))

(def partial** 
  (code-pog-fn ["f" "a"]
      (docstr [["f" "a 2-argument function"]
               ["a" "a value"]]
          "a 1-argument function that takes an argument b and returns (f a b)")
      "(function [b] (f a b))"))

(def compose*
  (code-pog-fn ["f" "g"]
      (docstr [["f" "a 1-argument function"]
               ["g" "a 1-argument function"]]
          "a 1-argument function that takes an argument x and returns (f (g x))")
               "(function [x] (f (g x)))"))
