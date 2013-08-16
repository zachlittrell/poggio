(ns poggio.functions.color
  (:import [com.jme3.math ColorRGBA])
  (:use [data color]
        [poggio.functions core utilities]))

(def color* (fn->pog-fn #(ColorRGBA. %1 %2 %3 1)
                        "color"
                        ["r" "g" "b"]))

(def red-value* (fn->pog-fn red "red-value" 
                            [{:name "c"
                              :type RGBA}]))
(def green-value* (fn->pog-fn green "green-value" 
                              [{:name "c"
                                :type RGBA}]))
(def blue-value* (fn->pog-fn blue "blue-value" 
                             [{:name "c"
                               :type RGBA}]))

(def red* (seq->pog-fn "red" [] 
              (docstr [] "the color red")
              (list color* (constantly* 1) 
                           (constantly* 0)
                           (constantly* 0))))
(def green* (seq->pog-fn "green" [] 
              (docstr [] "the color green")
              (list color* (constantly* 0) 
                           (constantly* 1)
                           (constantly* 0))))
(def blue* (seq->pog-fn "blue" [] 
              (docstr [] "the color blue")
              (list color* (constantly* 0) 
                           (constantly* 0) 
                           (constantly* 1))))

