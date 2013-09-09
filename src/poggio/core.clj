(ns poggio.core
  (:use [nifty-clj [builders :exclude [text]] elements events]
        [poggio main-menu])
  (:require [tools.level-viewer.core :as viewer]))

(defn -init [app nifty]
  (.addScreen nifty "main-menu" (main-menu nifty))
  (.gotoScreen nifty "main-menu"))

(defn -main [& args]
  (doto (viewer/make-app -init)
    (.start)))
