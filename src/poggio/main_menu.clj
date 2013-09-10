(ns poggio.main-menu
  (:require [tools.level-viewer.core :as level-viewer])
  (:use [clojure.java io]
        [nifty-clj [builders :exclude [text]] elements events]))

(defn -button [& {:keys [label id]}]
  (button :id id
          :label label
          :align :center
          :padding "10%"
          :width "35%"
          :height "15%"))

(defn main-menu [app nifty]
 (let [screen (build-screen nifty
               (screen
                 :layer
                 (layer :id "main-layer"
                        :child-layout :center
                        :panel
                         (panel :id "main-panel"
                                :child-layout :vertical
                                :align :center
                                :width "100%"
                                :controls
                                [(-button :id "new-game"
                                          :label "Start New Game")
                                 (-button :id "sandbox"
                                          :label "Sandbox Mode")
                                 (-button :id "credits"
                                          :label "Credits")])))
                :new-game {:on-left-click #(println "Can't play right now.")}
                :credits
                  {:on-left-click 
                     (fn [] (level-viewer/set-up-room! app #(load-string (slurp (resource "Levels/credits.clj")))  nifty))})
               ]
screen))
