(ns poggio.test.nifty-clj.textfieldx-test
  (:use [nifty-clj builders events textfieldx [elements :exclude [text]]])
  (:import [com.jme3.app SimpleApplication]))

(defn my-screen [nifty]
  (let [{:keys [initialize! textfieldx]} (textfieldx :id "HI" 
                                                     :width "300px"
                                                     :height "300px"
                                                     :text "Mr\nBlue\nSky")]
  (screen
    :controller (proxy [de.lessvoid.nifty.screen.DefaultScreenController] []
                  (onStartScreen []
                    (initialize! nifty)))
        :layer (layer :child-layout :center
             :panel
              (panel :child-layout :center
                     :control textfieldx)))))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren))
      (let [[_ nifty] (nifty this true)
            screen (.build (my-screen nifty) nifty)]
        (.addScreen nifty "crosshair" screen)
        (let [scroll (select screen "#nifty-scrollpanel-child-root")]
         );; (.layoutElements scroll)
         ;; (.setUp (nifty-control (select screen "HI") :scroll) 0 20 0 200 de.lessvoid.nifty.controls.ScrollPanel$AutoScroll/OFF))
        (.gotoScreen nifty "crosshair")))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
