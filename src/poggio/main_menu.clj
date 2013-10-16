(ns poggio.main-menu
  (:require [tools.level-viewer.core :as level-viewer])
  (:use [clojure.java io]
        [nifty-clj [builders :exclude [text]] elements events]))

(defn -button [& {:keys [label id visible?]
                  }]
  (button :id id
          :label label
          :align :center
          :padding "10%"
          :width "35%"
          :height "15%"))

(def levels
  [["Levels/level1.clj" "Introduction"]
   ["Levels/level2.clj" "Musica"]])

(defn enable! [screen max-level-unlocked]
  (doseq [[file name] (take (inc max-level-unlocked)
                            levels)]
    (.enable (select screen file))))

(defn main-menu [app nifty on-error! max-level-unlocked *current-level*]
 (let [load-level! (fn [level-path]
                     (reset! *current-level* level-path)
                     (level-viewer/set-up-room!
                       app
                       #(-> level-path
                            resource
                            slurp
                            load-string)
                       nifty
                       on-error!))
       screen (build-screen nifty
               (screen
                 :layers
                 [
                  (layer :id "main-layer"
                        :child-layout :center
                        :panel
                         (panel :id "main-panel"
                                :child-layout :vertical
                                :align :center
                                :width "100%"
                                :controls
                                [(-button :id "play"
                                          :label "Play")
                                 (-button :id "sandbox"
                                          :label "Sandbox Mode")
                                 (-button :id "credits"
                                          :label "Credits")]))

                 (layer :id "level-layer"
                        :child-layout :center
                        :visible? false
                        :panel
                         (panel 
                           :id "level-panel"
                     ;      :visible? false
                           :child-layout :vertical
                           :controls
                            (cons (label
                                    :text "Select a level"
                                    :color "#FFF")
                                  (concat (for [[file name] levels]
                                            (-button :id file
                                                     :label name))
                                          (list (-button :id "close-levels"
                                                         :label "Close"))))))
                                   ])
                :sandbox
                  {:on-left-click (partial load-level! "Levels/sandbox.clj")}
                :credits
                  {:on-left-click (partial load-level! "Levels/credits.clj")})
               ]
   (doseq [[file _] levels]
     (.disable (select screen file)))
   (enable! screen max-level-unlocked)
   (apply apply-interactions screen
     :play {:on-left-click
              (fn []
                (-> (select screen "level-layer")
                    (.show))
                (-> (select screen "main-layer")
                    (.disable))
                )}
     :close-levels {:on-left-click (fn []
                                     (-> (select screen "level-layer")
                                         (.setVisible false))
                                     (-> (select screen "main-layer")
                                         (.enable)))}
     (apply concat (for [[file name] levels]
                [file {:on-left-click (partial load-level! file)}])))
screen))
