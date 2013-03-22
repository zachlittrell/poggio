(ns poggio.function-box
  (:use [nifty-clj builders events]
        [poggio functions]))

(defn pog-fn->button [name f]
  (draggable :id name
             :background-color "#ff00ffff"
             :color            "#ffffffff"
             :child-layout :center
             :control (label :text name)))

(defn function-box [& functions]
  [(into {} functions)
    (panel :child-layout :vertical
                         :controls (map (partial apply pog-fn->button)
                                        functions))])


