(ns poggio.test.seesawx.values-gui-test
  (:use [seesaw core]
        [seesawx core]))

(defn -main [& args]
  (get-values {:label "Direction" 
               :id    :direction 
               :type  :direction}
              {:label "My Color"
               :id    :my-color
               :type  :color}))


