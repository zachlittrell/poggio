(ns poggio.functions.color
  (:import [com.jme3.math ColorRGBA])
  (:use [poggio.functions core utilities]))

(def color* (fn->pog-fn #(ColorRGBA. %1 %2 %3 1)
                        "color"
                        ["r" "g" "b"]))

(def red-value* (fn->pog-fn #(.getRed %) "red-value" ["c"]))
(def green-value* (fn->pog-fn #(.getGreen %) "green-value" ["c"]))
(def blue-value* (fn->pog-fn #(.getBlue %) "blue-value" ["c"]))

(def red* (seq->pog-fn "red" [] 
              (docstr [] "the color red")
              (list color* 1 0 0)))
(def green* (seq->pog-fn "green" [] 
              (docstr [] "the color green")
              (list color* 0 1 0)))
(def blue* (seq->pog-fn "blue" [] 
              (docstr [] "the color blue")
              (list color* 0 0 1)))

