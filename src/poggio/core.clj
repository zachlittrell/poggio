(ns poggio.core
  (:use [clojure.java io]
        [jme-clj audio physics physics-providers physics-control]
        [nifty-clj [builders :exclude [text]] elements events]
        [poggio main-menu]
        [poggio.functions gui])
  (:require [tools.level-viewer.core :as viewer])
  (:gen-class))


(defn -init [*current-level* *on-error!* max-levels-unlocked app nifty alert!]
  (.setDisplayFps app false)
  (.setDisplayStatView app false)
  (.addScreen nifty "main-menu" (main-menu app nifty alert! max-levels-unlocked *current-level*))
  (reset! *on-error!* alert!)
  (.gotoScreen nifty "main-menu")
  (play! app ["Music/06_Ghosts_I.ogg"]))

(defn -end-level [*current-level* *on-error!* *levels-unlocked* app nifty success?]
  (when (is-current-screen? nifty "main-menu")
    (.stop app))
  (when-not (is-current-screen? nifty "loading-screen")
   ; (clean! (.getCurrentScreen nifty) nifty)
    (pause-physics! app)
    (.removeAll (physics-space app) (.getRootNode app))
    (.detachAllChildren (.getRootNode app))
    (if success?
      ;;We only increase the index if this was a 
      (when-let [[i & more] (seq (keep-indexed 
                                   (fn [index [file name]]
                                     (when (= file @*current-level*)
                                       (inc index)))
                                   levels))]
        (when (and (< i (count levels))
                   (> i @*levels-unlocked*))
          (spit "savefile" i)
          (reset! *levels-unlocked* i)
          (enable! (.getScreen nifty "main-menu") i))
        
        (play! app ["Music/06_Ghosts_I.ogg"])
        (next-level! app nifty @*on-error!* *current-level*)
        )
      (do
        (play! app ["Music/06_Ghosts_I.ogg"])
        (.gotoScreen nifty "main-menu")))
    ))

(defn -main [& args]
  (.setLevel (java.util.logging.Logger/getLogger "com.jme3") 
             java.util.logging.Level/SEVERE)
  (let [*levels-unlocked* (atom (if (.exists (file "savefile"))
                                  (read-string (slurp "savefile"))
                                  0))
        *current-level* (atom nil)
        *alert* (atom nil)]
  (doto (viewer/make-app (partial -init *current-level* *alert* @*levels-unlocked*)
                         (partial -end-level *current-level* *alert* *levels-unlocked*)
                         :shutdown? true)
    (.start))))
