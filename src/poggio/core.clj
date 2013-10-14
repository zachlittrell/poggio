(ns poggio.core
  (:use [jme-clj audio]
        [nifty-clj [builders :exclude [text]] elements events]
        [poggio main-menu]
        [poggio.functions gui])
  (:require [tools.level-viewer.core :as viewer])
  (:gen-class))

(defn -init [app nifty alert!]
  (.setDisplayFps app false)
  (.setDisplayStatView app false)
  (.addScreen nifty "main-menu" (main-menu app nifty alert!))
  (.gotoScreen nifty "main-menu")
  (play! app ["Music/06_Ghosts_I.ogg"]))

(defn -end-level [app nifty]
 ; (clean! (.getCurrentScreen nifty) nifty)
  (.detachAllChildren (.getRootNode app))
  (.gotoScreen nifty "main-menu"))

(defn -main [& args]
  (.setLevel (java.util.logging.Logger/getLogger "com.jme3") 
             java.util.logging.Level/SEVERE)
  (doto (viewer/make-app -init -end-level :shutdown? true)
    (.start)))
