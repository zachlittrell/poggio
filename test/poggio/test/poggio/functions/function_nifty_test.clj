(ns poggio.test.poggio.functions.gui-function-test
  (:use [nifty-clj [builders :exclude [text]] elements events]
        [poggio.functions core gui])
  (:import [com.jme3.app SimpleApplication]
           [java.util.logging Level Logger]))

(defn my-screen [nifty]
  (let [[fn-map fn-box] (function-box ["[]" (fn->pog-fn (constantly [])
                                                        "[]"
                                                        [])]
                                      ["1" (fn->pog-fn (constantly 1)
                                                       "1"
                                                       [])]
                                      ["cons" (fn->pog-fn cons "cons" ["head"
                                                                       "tail"])]
                                      ["plus" (fn->pog-fn +' "plus" ["n1" "n2"])]
                                      ["compose" (fn->pog-fn comp* "compose" ["f" "g"])])
  made-screen
  (build-screen nifty
       (screen 
         :layer
         (layer
           :align :right
           :child-layout :center
           :id "fn-layer"
           :panel 
           (panel
             :id "fn-panels"
             :child-layout :vertical
             :width "33%"
             :align :right
           :panels [(doto fn-box (.align (keyword->align :left))
                                 (.width "33%"))
                    (panel :child-layout :horizontal
                           :align :right
                           :background-color "#fffffff"
                           :height "10%"
                           :control 
                             (droppable
                               :id "set-fn-pad"
                               :background-color "#694489"
                               :child-layout :horizontal
                               :control (label :text "Set Fn-Pad")))
                    (panel :child-layout :center
                           :id "fn-pad"
                           :background-color "#fffffff"
                           :align :left
                           :height "50%"
                           :control (control))
                    (panel :child-layout :center
                           :align :left
                           :height "10%"
                           :control 
                             (button :label "Compute"
                                      :id "compute"))]))))]
    (apply-interactions made-screen
      :compute {:on-left-click 
                #(when-let [[child & _] (seq (.getElements
                                             (select made-screen "fn-pad")))]
                    (println (invoke (pog-droppable->pog-fn
                                     child
                                     fn-map) []))
                   )}
      [:set-fn-pad :droppable] {:on-drop (fn [src draggable target] 
                                           (try
                                           (drag-pog-fn! nifty
                                                         draggable
                                                         fn-map
                                                         (-> target
                                                            (.getElement)
                                                            (.getParent)
                                                            (select :parent
                                                                    "fn-pad")
                                                            (.getElements)
                                                            (first)))
                                             (catch Exception e
                                               (.printStackTrace e))))})
                   made-screen))

    

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
