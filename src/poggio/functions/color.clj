(ns poggio.functions.color
  (:import [com.jme3.math ColorRGBA])
  (:use [data color]
        [poggio.functions core utilities parser utilities]))

(def color* (fn->pog-fn #(ColorRGBA. %1 %2 %3 1)
                        "color"
                        ["r" "g" "b"]
                        (docstr [["r" "a number between 0-1"] 
                                 ["g" "a number between 0-1"]
                                 ["b" "a number between 0-1"]]
                                "a color whose red, green, and blue values are r, g, and b")))

(def red-value* (fn->pog-fn red "red-value" 
                            [{:name "c"
                              :type RGBA}]
                           (docstr [["c" "a color"]] "the red value of c")))
(def green-value* (fn->pog-fn green "green-value" 
                              [{:name "c"
                                :type RGBA}]
                           (docstr [["c" "a color"]] "the green value of c")))

(def blue-value* (fn->pog-fn blue "blue-value" 
                             [{:name "c"
                               :type RGBA}]
                           (docstr [["c" "a color"]] "the green value of c")))

(def red* (code-pog-fn []
              (docstr [] "the color red")
              "color 1 0 0"))

(def green* (code-pog-fn [] 
              (docstr [] "the color green")
              "color 0 1 0"))

(def blue* (code-pog-fn [] 
              (docstr [] "the color blue")
              "color 0 0 1"))
