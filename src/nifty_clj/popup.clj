(ns nifty-clj.popup
  (:import [de.lessvoid.nifty.elements Element])
  (:use [data string]
        [nifty-clj builders [elements :only [select]]]))


(defn popup! 
  "Displays a popup with element."
  ([nifty element]
   (popup! nifty (.getCurrentScreen nifty) element))
  ([nifty screen element]
   (let [box-id (genstr "popup")
         container-id (genstr "container")
         button-id (genstr "yes-btn")
         box (popup :id box-id
                    :child-layout :center
                    :background-color "#000a"
                    :panel
                     (panel 
                       :child-layout :vertical
                       :background-color "#3C98A6"
                       :color "#000"
                       :align :center
                       :valign :center
                       :on-active-effect (effect :effect-name "border"
                                                 :effect-parameters
                                                 {"border" "1%"
                                                  "color"  "#AA6FA38D"})
                       :panels
                       [(panel :child-layout :center
                               :valign :center
                               :align :center
                               :id container-id)
                        (panel :child-layout :center
                               :align :center
                               :valign :bottom
                               :control (button :id button-id
                                                :label "Ok"))])
                    :register-popup nifty)
         popup (.createPopup nifty box-id)]
       (apply-interactions popup
          button-id {:on-left-click (fn []
                                      (.closePopup nifty
                                                   (.getId popup)))})
       (if (instance? Element element)
         (.add (select popup container-id) element)
         (build-with-screen nifty screen (select popup container-id) element))
       (.showPopup nifty screen (.getId popup) (select popup button-id)))))


(defn alert! [nifty message]
  "Displays a popup displaying the message"
  (popup! nifty (panel :child-layout :center
                       :text
                       (text :text message 
                             :height "45%"
                             :width "45%"
                             :font "Interface/Fonts/Default.fnt"
                             :wrap? true))))

