(ns poggio.test.nifty-clj.popup-test
  (:use [nifty-clj builders popup])
  (:import [com.jme3.app SimpleApplication]
           [java.util.logging Level Logger]))

(defn my-screen [nifty]
  (build-screen nifty
         (laid-out-screen-around
           :horizontal
           (button :label "Click me!"
                   :id "btn")
           (button label "Or me!"
                   :id "better-btn")
           (button label "I have more to say!"
                   :id "scroll-btn"))
         :btn {:on-left-click #(println (alert! nifty "HOWDY!\nThis is an alert."))}
         :scroll-btn {:on-left-click
                      #(alert! nifty "HOWDY\nTHIS IS MY MESSAGE FROM ME TO\n\n\n\n\n\n\n\n\n\n\n\n\nYou!" :scroll? true)}
         :better-btn {:on-left-click
                      #(popup! nifty (.getCurrentScreen nifty)
                              (panel :child-layout :center
                                     :text (text :text "HI!"))
                              :continuation (fn [_ val]
                                              (println val)))}))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (doto (.getFlyByCamera this)
        (.setDragToRotate true))
      (let [[_ nifty] (nifty this true)]
        (.addScreen nifty "my-screen" (my-screen nifty))
        (.gotoScreen nifty "my-screen")))))

(defn -main [& args]
  (.setLevel (Logger/getLogger "") Level/WARNING)
  (doto (make-app)
    (.start)))
