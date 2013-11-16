(ns poggio.functions.color
  (:import [com.jme3.math ColorRGBA])
  (:use [control assert]
        [data color]
        [poggio.functions core number utilities parser utilities]))

(def color* (fn->pog-fn #(do
                           (assert! (in-bounds? %1 0 255))
                           (assert! (in-bounds? %2 0 255))
                           (assert! (in-bounds? %3 0 255))
                           (ColorRGBA. (/ %1 255.0) (/ %2 255.0) (/ %3 255.0) 1))
                        "color"
                        [{:name "r" :type Number}
                         {:name "g" :type Number}
                         {:name "b" :type Number}]
                        (docstr [["r" "a number between 0-255"] 
                                 ["g" "a number between 0-255"]
                                 ["b" "a number between 0-255"]]
                                "a color whose red, green, and blue values are r, g, and b")))

(def red-value* (fn->pog-fn (fn [c] (* (red c) 255)) "red-value" 
                            [{:name "c"
                              :type RGBA}]
                           (docstr [["c" "a color"]] "the red value of c")))
(def green-value* (fn->pog-fn (fn [c] (* (green c) 255)) "green-value" 
                              [{:name "c"
                                :type RGBA}]
                           (docstr [["c" "a color"]] "the green value of c")))

(def blue-value* (fn->pog-fn (fn [c] (* (blue c) 255)) "blue-value" 
                             [{:name "c"
                               :type RGBA}]
                           (docstr [["c" "a color"]] "the blue value of c")))

(def red* (code-pog-fn []
              (docstr [] "the color red")
              "(color 255 0 0)"))

(def green* (code-pog-fn [] 
              (docstr [] "the color green")
              "(color 0 255 0)"))

(def blue* (code-pog-fn [] 
              (docstr [] "the color blue")
              "(color 0 0 255)"))

(def color-equal?* (code-pog-fn [{:name "c1" :type RGBA}
                                {:name "c2" :type RGBA}]
                    (docstr [["c1" "a color"] ["c2" "a color"]]
                      "true if c1 is equal to c2.")
"(and (and (equal? (r-value c1) (r-value c2))
           (equal? (g-value c1) (g-value c2)))
      (equal? (b-value c1) (b-value c2)))"))
