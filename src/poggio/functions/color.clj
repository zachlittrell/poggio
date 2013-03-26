(ns poggio.functions.color
  (:import [com.jme3.math ColorRGBA])
  (:use [poggio.functions core]))

(def color* (fn->pog-fn #(ColorRGBA. %1 %2 %3 1)
                        "color"
                        ["r" "g" "b"]))

(def red* (fn->pog-fn #(.getRed %) "red" ["c"]))
(def green* (fn->pog-fn #(.getGreen %) "green" ["c"]))
(def blue* (fn->pog-fn #(.getBlue %) "blue" ["c"]))

