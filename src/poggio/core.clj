(ns poggio.core
  (:use [nifty-clj [builders :exclude [text]] elements events]
        [poggio main-menu])
  (:require [tools.level-viewer.core :as viewer]))

(defn -init [app nifty]
  (.addScreen nifty "main-menu" (main-menu app nifty))
  (.gotoScreen nifty "main-menu"))

(defn -end-level [app nifty]
  (.detachAllChildren (.getRootNode app))
  (.gotoScreen nifty "main-menu"))


(defn -main [& args]
  (doto (viewer/make-app -init -end-level)
    (.start)))
