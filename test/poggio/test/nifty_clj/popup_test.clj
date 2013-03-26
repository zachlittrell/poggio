(ns poggio.test.nifty-clj.popup-test
  (:use [nifty-clj builders popup])
  (:import [com.jme3.app SimpleApplication]
           [java.util.logging Level Logger]))

(defn my-screen [nifty]
  (build-screen nifty
         (screen-around
           (button :label "Click me!"
                   :id "btn"))
         :btn {:on-left-click #(alert! nifty "HOWDY!\nThis is an alert.")}))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (doto (.getFlyByCamera this)
        (.setDragToRotate true))
      (let [nifty (nifty this true)]
        (.addScreen nifty "my-screen" (my-screen nifty))
        (.gotoScreen nifty "my-screen")))))

(defn -main [& args]
  (.setLevel (Logger/getLogger "") Level/WARNING)
  (doto (make-app)
    (.start)))
