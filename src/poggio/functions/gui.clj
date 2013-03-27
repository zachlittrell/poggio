(ns poggio.functions.gui
  (:use [nifty-clj [builders :exclude [text]] elements events]
        [poggio.functions core]))

(def border-effect (effect :effect-name "border"
                           :effect-parameters {"color" "#694489"
                                               "border" "1px"}))



(defn pog-fn->draggable [name f]
  (draggable :id name
             :background-color "#ff00ffff"
             :color            "#ffffffff"
             :on-active-effect border-effect
             :child-layout :center
             :valign :center
             :control (label :text name)))

(defn pog-fn->droppable [name f]
  (panel    :background-color "#ff00ffff"
            :color            "#ffffffff"
           ; :on-active-effect border-effect
            :id ""
            :child-layout :horizontal
            :valign :center
            :padding "1px"
            :panels  (list*
                       (panel :child-layout :horizontal
                              :align :left
                              :valign :center
                              :control 
                         (label :align :left
                                :valign :center
                                :padding "1px"
                                :text    name))
                       (for [param (parameters f)]
                   (panel :child-layout :horizontal
                            :align :left
                            :background-color "#990066"
                            :control
                     (droppable 
                       :id param
                       :align :left
                       :valign :center
                       :child-layout :horizontal
                       :padding "1px"
                  ;      :on-active-effect border-effect
                       :control (label :text param)))))))

(defn function-box [& functions]
  [(into {} functions)
    (panel :child-layout :vertical
           :padding "1px"
                         :controls (map (partial apply pog-fn->draggable)
                                        functions))])

(defn drag-pog-fn! [nifty pog-draggable fn-map droppable-element]
  (let [draggable (.getElement pog-draggable)
        parent (.getParent droppable-element)
        name (.getId draggable)
        f (fn-map name)]
    ;(remove-element! droppable-element)
    (apply build nifty parent
        (doto (pog-fn->droppable name f))
          ;;(.height "10%"))
        (apply concat
            (for [param (parameters f)]
              [[param :droppable]
               {:on-drop (fn [src draggable target]
                           (drag-pog-fn! nifty draggable fn-map (.getElement
                                                                  target)))}])))
    (remove-element! droppable-element)
  false))

(defn pog-droppable->pog-fn [pog-draggable fn-map]
  (let [[label & params] (.getElements pog-draggable)
        id (text (first-child label))
        f (fn-map id)]
    (partial* f (into {} (for [[param-elem param-name] (map list params
                                                                (parameters f))
                               :let [param-elem (first (.getElements param-elem))]
                               :when (= "" (.getId param-elem))
                               :let [[label & more] (.getElements param-elem)]]
                           [param-name
                            (pog-droppable->pog-fn param-elem fn-map)])))))

