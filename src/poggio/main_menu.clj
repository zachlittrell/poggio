(ns poggio.main-menu
  (:require [tools.level-viewer.core :as level-viewer])
  (:use [clojure.java io]
        [nifty-clj [builders :exclude [text]] elements events]))

(defn -button [& {:keys [label id visible? height]
                  :or {height "15%"}}]
  (button :id id
          :label label
          :align :center
          :padding "10%"
          :width "35%"
          :height height))

(def levels
  [["Levels/orientation.clj" "Orientation"]
   ["Levels/functionjunction.clj" "Function Junction"]
   ["Levels/truthbeknown.clj" "Truth Be Known"]
   ["Levels/musica.clj" "Musica"]
   ["Levels/growth.clj" "Growth"]
   ["Levels/theend.clj" "The End"]])

(defn enable! [screen max-level-unlocked]
  (doseq [[file name] (take (inc max-level-unlocked)
                            levels)]
    (.enable (select screen file))))

(defn load-level! 
  [app nifty on-error! *current-level* level-path]
  (reset! *current-level* level-path)
  (level-viewer/set-up-room!
    app
    #(-> level-path resource slurp load-string)
    nifty 
    on-error!))

(defn level-index
  [level-name]
  (first (keep-indexed 
           (fn [index [file name]]
             (when (= file level-name)
               index))
           levels)))

(defn next-level!
  [app nifty on-error! *current-level*]
  (let [current-level @*current-level*
        index (inc (level-index current-level))]
    (if (< index (count levels))
      (load-level! app nifty on-error! *current-level* (first (levels (inc index))))
      (.gotoScreen nifty "main-menu"))))



(defn main-menu [app nifty on-error! max-level-unlocked *current-level*]
 (let [load-level! (partial load-level! app nifty on-error! *current-level*)
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
                                                     :height "10%"
                                                     :label name))
                                          (list (-button :id "close-levels"
                                                         :height "10%"
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
