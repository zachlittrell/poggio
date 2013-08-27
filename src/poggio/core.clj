(ns poggio.core
  (:require [tools.level-viewer.core :as viewer]))

(defn -main [& args]
  (viewer/-main "test/poggio/test/poggio/levels/sliding_doors_level.clj"))
