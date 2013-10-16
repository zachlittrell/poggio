(ns poggio.core
  (:use [clojure.java io]
        [jme-clj audio]
        [nifty-clj [builders :exclude [text]] elements events]
        [poggio main-menu]
        [poggio.functions gui])
  (:require [tools.level-viewer.core :as viewer])
  (:gen-class))


(defn -init [max-levels-unlocked app nifty alert!]
  (.setDisplayFps app false)
  (.setDisplayStatView app false)
  (.addScreen nifty "main-menu" (main-menu app nifty alert! max-levels-unlocked))
  (.gotoScreen nifty "main-menu")
  (play! app ["Music/06_Ghosts_I.ogg"]))

(defn -end-level [*levels-unlocked* app nifty success?]
  (when (is-current-screen? nifty "main-menu")
    (.stop app))
  (when-not (is-current-screen? nifty "loading-screen")
   ; (clean! (.getCurrentScreen nifty) nifty)
    (.detachAllChildren (.getRootNode app))
    (.gotoScreen nifty "main-menu")))

(defn -main [& args]
  (.setLevel (java.util.logging.Logger/getLogger "com.jme3") 
             java.util.logging.Level/SEVERE)
  (let [*levels-unlocked* (atom (if (.exists (file "savefile"))
                                  (read-string (slurp "savefile"))
                                  0))]
  (doto (viewer/make-app (partial -init @*levels-unlocked*)
                         (partial -end-level *levels-unlocked*)
                         :shutdown? true)
    (.start))))
