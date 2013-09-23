(ns tools.level-editor.core
  (:import [java.awt Color]
           [java.awt.image BufferedImage]
           [javax.swing ImageIcon])
  (:use [clojure pprint]
        [data string]
        [seesaw chooser core]
        [seesawx core]
        [tools.level-editor templates]
        [tools.level-viewer [core :only [view-level]]]))
;  (:gen-class))

(def max-x 33)
(def max-y 33)

(def default-image (image-icon-pad* [100 100] {}))

(def default-item-icons
  {:wall (image-icon-pad* [100 100] {}
           (.drawRect 0 0 99 99)
           (.drawLine 0 0 99 99))
   :player (image-icon-pad* [100 100] 
                      {:questions [{:id      :direction 
                                    :type    :direction 
                                    :label   "Enter Direction"}]}
             (.drawString  "☺" 49 49))
   :blank  default-image})



       
(defn template->item-icon [[name template]]
  (let [{:keys [image]} template]
    (image-icon* image (assoc template :name name))))

(defn answer-questions [questions answers]
  (for [{:keys [id type] :as question} questions
        :let [answer (answers id)]]
    (do
    (assoc question :type (if (keyword? type)
                            ;;If type was a keyword, we add the init key
                            [type :init answer]
                            (let [index (.indexOf type :init)]
                              (if (< -1 index)
                                ;;If there is already an init, replace it
                                (assoc type (inc index) answer)
                                ;;Else, add the init key.
                                (conj type :init answer)))))))
  )

(defn- config-loc! [grid x z template answers]
  (config! (get-grid-component grid x z)
           :icon (image-icon* 
                   (:image template)
                    (assoc template
                           :answers answers))))
                                   


(defn load-level! [{:keys [loc dir walls widgets]} grid]
  ;;Clean out the place first!
  (doall 
    (for-grid-panel [[row column c] grid]
       (config! c
          :icon (:blank default-item-icons))))
  ;;Place the player
  (config-loc! grid (second loc) (first loc) 
               (meta (:player default-item-icons))
               {:direction dir})
  ;;Place the walls
  (doseq [[x z] walls]
    (config-loc! grid z x  (meta (:wall default-item-icons)) {}))
  ;;Place the widgets
  (doseq [{:keys [name answers]} widgets]
    (config-loc! grid (:z answers) (:x answers)
                 (assoc (keyword->widget-template name)
                        :name name)
                 answers)))

  

(defn item-panel []
  (listbox :model (concat (vals default-item-icons)
                          (map template->item-icon keyword->widget-template))))

(defn update-selected! [item-box e]
  (config! (.getComponent e)
      :icon (condp == (.getButton e)
              java.awt.event.MouseEvent/BUTTON3
              (let [icon (.getIcon (.getComponent e))
                    m (meta icon)
                    answers (:answers m)]
                (if answers
                  (image-icon* (.getImage icon)
                     (if-let [answers (apply get-values
                                             (answer-questions (:questions m)
                                                               answers))]
                       (assoc m :answers answers)
                       m))
                  icon))
              java.awt.event.MouseEvent/BUTTON1
              (let [icon (selection item-box)
                     m (meta icon)
                     questions (:questions m)
                     answers (when questions
                               (apply get-values questions))]
                (if (and (seq questions) (not answers))
                 (.getIcon (.getComponent e))
                 (image-icon* (.getImage icon)
                              (assoc m :answers answers)))))))
(defn map-panel [item-box]
  (let [items (for [n (range (* max-x max-y))]
                (label 
                  :background Color/WHITE
                  :icon default-image
                  :listen [:mouse-clicked (partial update-selected! item-box)]))]
     (grid-panel :columns max-x
                 :rows max-y
                 :items items)))

(defn output-panel [map-panel]
  (text :multi-line? true
        :editable? false))

(defn code [map-panel]
  (let [player    (.getImage (:player default-item-icons))
        [loc dir] (if-let [[row column c] 
                           (some-in-grid-panel 
                             #(identical? player 
                                          (.getImage (config % :icon)))
                             map-panel)]
                    [[column row]
                    (:direction (:answers (meta (config c :icon))))]
                 (throw (Exception. "Need to position player.")))

        walls    (into #{} 
                   (let [wall (.getImage (:wall default-item-icons))]
                     (for-grid-panel [[row column component] map-panel
                                      :when (identical? 
                                              wall 
                                                (.getImage (config component :icon)))]
                      [column row])))
      widgets   (into []
                      (for-grid-panel [[row column component] map-panel
                                       :let [icon (config component :icon)
                                             m (meta icon)
                                             name (:name m)
                                             answers (:answers m)]
                                       :when name]
                        {:name name
                         :answers (assoc answers :x column
                                                 :z row)}))
                 ;;(build [column row] answers)))
      ;;prelude (for-grid-panel [[row column component] map-panel
      ;;                         :let [icon (config component :icon)
      ;;                               m (meta icon)
      ;;                               prelude (:prelude m)]
      ;;                         :when prelude]
      ;;          prelude)
        ]
          ;(list* 'do prelude)
          {:loc loc
           :dir dir
           :walls walls
           :wall-mat "Textures/paper1.jpg"
           :widgets widgets}))
     

(defn view-button [map-panel]
  (action :name "View"
          :handler
          (fn [_]
            (view-level (eval (code map-panel))))))

(defn build-button [map-panel output-panel]
  (action :name "Build"
          :handler 
          (fn [_]
            (config! output-panel :text
                     (object->pretty-printed-string (code map-panel))))))

(defn load-button [map-panel]
  (action :name "Load" 
          :handler
          (fn [_]
             (choose-file 
               :success-fn (fn [_ file]
                             (load-level! (read-string (slurp file)) map-panel))))))

(defn save-button [map-panel]
  (action :name "Save"
          :handler
          (fn [_]
            (choose-file
              :type :save
              :success-fn (fn [_ file]
                            (spit file (object->pretty-printed-string
                                         (code map-panel))))))))

(defn make-gui []
  (let [item-box (item-panel)
        map-panel  (map-panel item-box)
        output-panel (output-panel map-panel)
        load-button (load-button map-panel)
        save-button (save-button map-panel)
        build-button (build-button map-panel output-panel)
        view-button (view-button map-panel)]
  (frame :title "Poggio Level Editor"
         :on-close :exit
         :size [500 :by 500]
         :content (border-panel :west (scrollable item-box)
                                :center (top-bottom-split 
                                          (scrollable map-panel)
                                          (scrollable output-panel)
                                          :divider-location 0.5)
                                :north (flow-panel 
                                         :items [ load-button
                                                  save-button
                                                  build-button
                                                  view-button])))))

(defn -main [& args]
  (-> (make-gui)
      (show!)))

