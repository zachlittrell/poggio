(ns nifty-clj.widgets
  (:use [nifty-clj builders]))

(defn button-panel [lbl & buttons]
  (panel :child-layout :vertical
         :panels [(panel :child-layout :center
                         :control (label :text lbl))
                  (panel :child-layout :horizontal
                         :controls
                           (for [[id lbl] buttons]
                             (button :label lbl
                                     :id id)))]))
