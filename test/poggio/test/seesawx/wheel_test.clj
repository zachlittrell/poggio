(ns poggio.test.seesawx.wheel-test
  (:use [seesaw core]
        [seesawx core]))

(defn -main [& args]
  (get-values "Dir" :direction))
