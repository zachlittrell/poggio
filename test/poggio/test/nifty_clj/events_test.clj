(ns poggio.test.nifty-clj.events-test
  (:use [nifty-clj builders])
  (:import [com.jme3.app SimpleApplication]
           [java.util.logging Level Logger]))

(defn my-screen [nifty]
  (build nifty
         (screen-around
           (button :label "Click me!"
                   :id "btn"))
         :btn [:on-left-click #(println "Hey hey hey")]))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (doto (.getFlyByCamera this)
        (.setDragToRotate true))
      (let [nifty (nifty this true)]
        (.addScreen nifty "my-screen" (my-screen nifty))
        (.gotoScreen nifty "my-screen")
        (println "HEEEY" (-> (.getCurrentScreen nifty)
                             (.findElementByName "btn")
                             (.getWidth)))))))

(defn -main [& args]
  (.setLevel (Logger/getLogger "") Level/WARNING)
  (doto (make-app)
    (.start)))
