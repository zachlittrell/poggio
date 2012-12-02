(ns poggio.test.nifty-clj.crosshair-test
  (:use [nifty-clj builders])
  (:import [com.jme3.app SimpleApplication]))

(def cross-hair-screen
  (screen-around
    (label :text "+")))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (let [nifty (nifty this true)]
        (.addScreen nifty "crosshair" (.build cross-hair-screen nifty))
        (.gotoScreen nifty "crosshair")))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
