(ns data.color
  (:import [com.jme3.math ColorRGBA]
           [de.lessvoid.nifty.tools Color]))

(defprotocol RGBA
  "A protocol to unify the different Color classes."
  (red [c])
  (green [c])
  (blue [c])
  (alpha [c]))

(extend-protocol RGBA
  ColorRGBA
  (red [c] (.getRed c))
  (green [c] (.getGreen c))
  (blue [c] (.getBlue c))
  (alpha [c] (.getAlpha c))
  Color
  (red [c] (.getRed c))
  (green [c] (.getGreen c))
  (blue [c] (.getBlue c))
  (alpha[c] (.getAlpha c))
  java.awt.Color
  (red [c] (.getRed c))
  (green [c] (.getGreen c))
  (blue [c] (.getBlue c))
  (alpha [c] (.getAlpha c)))

(defn color->triple
  "Returns a triple containing the red, green, and blue value of the color"
  [color]
  [(red color) (green color) (blue color)])

(defn color->quadruple
  "Returns a quadruple containing the red, green, blue, and alpha value
   of the color."
  [color]
  (conj (color->triple color) (alpha color)))

(defn rgb-equal? 
  "Returns true if the colors' rgb components are equal."
  [color1 color2]
  (and (== (red color1)
           (red color2))
       (== (green color1)
           (green color2))
       (== (blue color1)
           (blue color2))))
