(ns poggio.test.nifty-clj.textfieldx-test
  (:use [nifty-clj builders textfieldx [elements :exclude [text]]])
  (:import [com.jme3.app SimpleApplication]))

(def my-screen
  (screen-around
    (textfieldx-component :id "HI"
                          :text "HI\nGoodbye\nBlue Skies"
                          :width "300px"
                          :height "300px")))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (let [[_ nifty] (nifty this true)
            screen (.build my-screen nifty)]
        (.addScreen nifty "crosshair" screen)
        (let [scroll (select screen "#nifty-scrollpanel-child-root")]
         );; (.layoutElements scroll)
         ;; (.setUp (nifty-control (select screen "HI") :scroll) 0 20 0 200 de.lessvoid.nifty.controls.ScrollPanel$AutoScroll/OFF))
        (.gotoScreen nifty "crosshair")))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
