(ns jme-clj.bitmap-text
  "Functions for creating BitmapText objects."
  (:use [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.font BitmapFont BitmapText]))

(def-opts-constructor bitmap-text
  :setter
  {:font `(BitmapFont.)}
  `(BitmapText. ~'font)
  {:font :no-op})
