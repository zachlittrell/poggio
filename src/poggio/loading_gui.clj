(ns poggio.loading-gui
  (:use [nifty-clj [builders :exclude [text]] elements]))

(def fade-in
  (effect 
    :effect-name "fade"
    :effect-parameters {"start" "#0"
                        "end" "#f"}))
(def fade-out
  (effect
    :effect-name "fade"
    :effect-parameters {"start" "#f"
                        "end" "#0"}))

(defn loading-gui []
  (screen-around-panels
    (panel :child-layout :vertical
           :align :center
           :width "50%"
           :panels
            [(panel :child-layout :center
                    :align :center
                    :width "100%"
                    :control (label :wrap? true 
                                    :width "100%" 
                                    :id "header"
                                    :color "#00"
                                    :on-start-screen-effect fade-in
                                    :on-end-screen-effect fade-out))
             (panel :child-layout :center
                    :width "100%"
                    :align :center
                    :control (label 
                               :wrap? true 
                               :width "100%" 
                               :color "#00"
                               :on-start-screen-effect fade-in
                               :on-end-screen-effect fade-out
                               :id "subheader"))])))

(defn set-headers! [loading-gui & {:keys [header subheader]}]
  (set-text! (select loading-gui "header") header)
  (set-text! (select loading-gui "subheader") subheader)
  (.layoutLayers loading-gui))
    
