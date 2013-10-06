(ns tools.level-editor.templates
  (:use [tools.level-editor.widgets 
         [ball-stereo :only [ball-stereo-template]]
         [function-cannon :only [function-cannon-template]]
         [globule-receiver :only [globule-receiver-template]]
         [glass-door :only [glass-door-template]]
         [color-screen :only [color-screen-template]]
         [music-box :only [music-box-template]]
         [text-screen :only [text-screen-template]]]))

(def keyword->widget-template
  (array-map
   :function-cannon function-cannon-template
   :globule-receiver globule-receiver-template
   :glass-door glass-door-template
   :text-screen text-screen-template
   :music-box music-box-template
   :ball-stereo ball-stereo-template
   :color-screen color-screen-template))

(defn eval-level 
  "Replaces widgets key in level-map with build functions"
  [level-map]
  (assoc level-map :widgets
    (for [{:keys [name answers]} (:widgets level-map)]
      (fn [app]
        ((:build (keyword->widget-template name))
         (assoc answers :app app))))))
