(ns nifty-clj.widgets
  (:import [de.lessvoid.nifty.tools Color])
  (:require [nifty-clj.builders :as builder])
  (:use [data color]))

(defn wbutton
  "Returns a control that acts like a button."
  [& options]
  (let [{:keys [id background-color color label
                width height]
         :or {background-color "#734f96"
              color "#fff"
              label "Click"}} options
        background-color* (if (string? background-color)
                            (Color. background-color)
                            background-color)
        [bgr bgg bgb bga] (color->quadruple background-color*)
        background-color** (Color. (- bgr 0.2) (- bgg 0.2) (- bgb 0.2)
                                   bga)
        color (if (string? color) (Color. color) color)]
    (builder/button 
      :id id
      :label label
  ;;    :height height
  ;;    :width width
      :background-color background-color*
      :visible-to-mouse? true
      :color color
      :on-click-effect
        (builder/effect :effect-name "colorPulsate"
                        :effect-parameters 
                          {"startColor" (.getColorString background-color*)
                           "endColor" (.getColorString background-color**)
                           "cycle" "false"
                           "period" "300"}))))

