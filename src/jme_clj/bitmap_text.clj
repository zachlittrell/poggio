(ns jme-clj.bitmap-text
  "Functions for creating BitmapText objects."
  (:use [data enum]
        [jme-clj assets]
        [control.defutilities :only [def-opts-constructor]])
  (:import [com.jme3.font BitmapFont BitmapText]))

(def-enum-keyword-map keyword->align
                      com.jme3.font.BitmapFont$Align)

(def-opts-constructor bitmap-text
  [:setter]
  {:font `(BitmapFont.)}
  `(BitmapText. ~'font)
  {:font [:no-op]
   :alignment [:setter
               :thread-in `(keyword->align)]})

(def-opts-constructor bitmap-font
  [:setter]
  {:asset-manager nil
   :font-name "Interface/Fonts/Default.fnt"}
  `(.loadFont (asset-manager ~'asset-manager) ~'font-name)
  {:asset-manager [:no-op]
   :font-name [:no-op]})
