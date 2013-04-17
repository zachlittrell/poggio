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
                            :valign :center
                            :background-color "#990066"
                            :control
                     (droppable 
                       :id (name* param)
                       :align :left
                       :valign :center
                       :child-layout :horizontal
                       :padding "1px"
                  ;      :on-active-effect border-effect
                       :control (label :text (name* param))))))))

(defn function-box [& functions]
  [(into {} functions)
    (panel :child-layout :vertical
           :padding "1px"
                         :controls (map (partial apply pog-fn->draggable)
                                        functions))])

(defn drag-pog-fn! 
  ([nifty pog-draggable fn-map droppable-element]
   (let [draggable (.getElement pog-draggable)]
     (drag-pog-fn! nifty (.getId draggable)
                   (fn-map (.getId draggable)) fn-map droppable-element)))
  ([nifty name f fn-map droppable-element]
    (let [parent (.getParent droppable-element)]
      (apply build nifty parent
          (doto (pog-fn->droppable name f))
            ;;(.height "10%"))
          (apply concat
              (for [param (parameters f)]
                [[(name* param) :droppable]
                 {:on-drop (fn [src draggable target]
                             (try
                             (drag-pog-fn! nifty draggable fn-map (.getElement
                                                                    target))
                               (catch Exception e
                                 (.printStackTrace e))))}])))
    (remove-element! droppable-element)
  false)))

(defn pog-droppable->pog-fn [pog-draggable fn-map]
  (let [[label & params] (.getElements pog-draggable)
        id (text (first-child label))
        f (fn-map id)]
    (partial* f (into {} (for [[param-elem param-name] (map list params
                                                                (parameters f))
                               :let [param-elem (first (.getElements param-elem))]
                               :when (= "" (.getId param-elem))
                               :let [[label & more] (.getElements param-elem)]]
                           [(name* param-name)
                            (pog-droppable->pog-fn param-elem fn-map)])))))


(defn function-screen [nifty & functions]
  (let [[fn-map fn-box] (apply function-box functions)
        *fn-map* (atom fn-map)
        made-screen
          (build-screen nifty
            (screen
             :layer
              (layer
                :align :right
                :child-layout :center
                :id "fn-layer"
                :panel
                (panel
                  :id "fn-panels"
                  :child-layout :vertical
                  :width "33%"
                  :align :right
                  :panels
                  [(doto fn-box (.align (keyword->align :left))
                                (.width "33%"))
                   (panel
                     :child-layout :center
                     :id "fn-pad"
                     :background-color "#fffffff"
                     :align :left
                     :height "50%"
                     :control (control))
                   (panel
                     :child-layout :center
                     :align :left
                     :height "10%"
                     :control
                     (button :label "Compute"
                             :id "compute"))]))))]
    (apply-interactions made-screen
      :compute {:on-left-click
                #(when-let [[child & _] (seq (.getElements
                                             (select made-screen "fn-pad")))]
                   (let [pog-fn (pog-droppable->pog-fn child @*fn-map*)]
                     (when (empty? (parameters pog-fn))
                       (invoke (pog-droppable->pog-fn child @*fn-map*) []))))})
    [*fn-map* made-screen]))

(defn set-current-function! [nifty function-screen pog-fn *fn-map*]
  (drag-pog-fn! nifty " " pog-fn 
                (swap! *fn-map* assoc " " pog-fn) 
                (first (.getElements (select function-screen "fn-pad")))))


