(ns nifty-clj.popup
  (:import [de.lessvoid.nifty.elements Element])
  (:use [data coll string]
        [nifty-clj builders [elements :only [select]]]))


(defn popup! 
  "Displays a popup with element."
  ([nifty element]
   (popup! nifty (.getCurrentScreen nifty) element))
  ([nifty screen element & opts]
   (let [{:keys [selections default continuation] 
          :or
          {selections ["Ok"]
           default "Ok"}} opts
         selections-ids (map genstr selections)
         box-id (genstr "popup")
         container-id (genstr "container")
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
                               :controls 
                                 (for [[name id] (zip selections
                                                      selections-ids)]
                                           (button :id id
                                                   :label name)))])
                    :register-popup nifty)
         popup (.createPopup nifty box-id)]
       (apply apply-interactions popup
          (flatten 
            (for [[name id] (zip selections selections-ids)]
               [id {:on-left-click 
                     (fn []
                       (.closePopup nifty
                                   (.getId popup))
                       (when continuation
                         (continuation name (select popup container-id))))}])))
       (if (instance? Element element)
         (.add (select popup container-id) element)
         (build-with-screen nifty screen (select popup container-id) element))
       (.showPopup nifty screen (.getId popup) (select popup 
                                                       (first selections-ids))))))



(defn alert! [nifty message & {:keys [scroll?]
                               :or {:scroll? false}}]
  "Displays a popup displaying the message.
   Can pass :scroll? flag."
  (let [txt (text :text message
                  :font "Interface/Fonts/Default.fnt"
                  :wrap? true)]
    (popup! nifty  (if-not scroll?
                     (panel :child-layout :center
                         :text (doto txt
                                 (.height "45%")
                                 (.width "45%")))
                     (panel :child-layout :center
                            :control (scroll-panel 
                                         :style "nifty-listbox"
                                         :id "popup-scroller"
                                         :width "45%"
                                         :height "45%"
                                         :control 
                                           (label :text message
                                                  :align :left
                                                  :text-h-align :left
                                                  :text-v-align :top
                                                  :wrap? true)
                                                     ))))))

;;(defn input! [nifty message initial continuation]
;;  "Displays a popup displaying the message, and passing 
;;   whatever is given to a textbox (starting with initial) to 
;;   continuation, or nil if Cancel is pressed."
;;  (popup! nifty (.getCurrentScreen nifty)
;;          (panel :child-layout :vertical
;;                 :controls [(text :text message
;;                                  :height "45%"
;;                                  :width "45%"
;;                                  :font "Interface/Fonts/Default.fnt"
;;                                  :wrap? true)
;;                            (text-field :id "input-box"
;;                                        :text initial)])
;;          :selections ["Ok" "Cancel"]
;;          :continuation (fn [selected popup]
;;                          (if (= selected "Ok")
;;                            (continuation (elements/text 
;;                                            (select popup "input-box")))
;;                            (continuation nil)))))
;;
