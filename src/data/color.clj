(ns data.color
  (:import [com.jme3.math ColorRGBA]))

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
  (alpha [c] (.getAlpha c)))

(defn color->triple [color]
  "Returns a triple containing the red, green, and blue value of the color"
  [(red color) (green color) (blue color)])
