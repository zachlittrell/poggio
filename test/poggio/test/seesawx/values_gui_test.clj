(ns poggio.test.seesawx.values-gui-test
  (:use [seesaw core]
        [seesawx core]))

(defn -main [& args]
  (get-values {:label "Direction" 
               :id    :direction 
               :type  :direction}
              {:label "My Color"
               :id    :my-color
               :type  :color}
              {:label "My ID"
               :id    :my-id
               :type  :string}
              {:label "Organ Donor?"
               :id    :organ-donor?
               :type  :boolean}
              {:label "Favorite Color"
               :id    :favorite-color
               :type  [:choice
                       :model [:red
                               :blue
                               :orange
                               :purple]]}
              {:label "List of places you've been"
               :id    :places
               :type  [:list 
                       :type :string]}
              {:label "Age?"
               :id    :age
               :type  :integer}))
