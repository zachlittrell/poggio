(ns poggio.test.poggio.function-box-test
  (:use [nifty-clj builders events]
        [poggio functions function-box])
  (:import [com.jme3.app SimpleApplication]
           [java.util.logging Level Logger]))

(defn my-screen [nifty]
  (let [[fn-map fn-box] (function-box ["inc" (fn->pog-fn inc "inc" ["n"])]
                                      ["dec" (fn->pog-fn dec "dec" ["n"])]
                                      ["compose" (fn->pog-fn comp* "compose" ["f" "g"])])]
  (build nifty
       (screen 
         :controller (droppable-drop-filter
                        (fn [src draggable target]
                          (println "HEY" src
                                   draggable
                                   target)
                          false))
         :layer
         (layer
           :align :right
           :child-layout :vertical
           :panels [(doto fn-box (.align (keyword->align :right))
                                 (.width "33%"))
                    (panel :child-layout :center
                           :align :right
                           :width "33%"
                           :control 
                             (droppable 
                               :id "fn-pad"
                               :background-color "#fffffff"
                               ))]))
      [:fn-pad :droppable] {:on-drop (fn [s d t] 
                                      (println "Dropped" s d t)`
                                      false)})))

    

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
