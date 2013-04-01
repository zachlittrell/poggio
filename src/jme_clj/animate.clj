(ns jme-clj.animate
  (:use [jme-clj physics physics-control]))

(defn visibility! [app geom show?]
  (if (and show? (not (.getParent geom)))
    (do 
      (if-let [ctrl (physics-control geom)]
        (.setEnabled ctrl true))
      (attach! app geom))
    (when (.getParent geom)
      (detach! geom))))
