(ns tools.level-editor.core
  (:import [java.awt Color]
           [java.awt.image BufferedImage]
           [javax.swing ImageIcon])
  (:use [seesaw core]
        [seesawx core]
        [tools.level-editor templates]
        [tools.level-viewer [core :only [view-level]]]))

(def max-x 13)
(def max-y 13)

(def default-image (image-icon-pad [100 100] {}))

(def default-item-icons
  {:wall (image-icon-pad [100 100] {}
           (.drawRect 0 0 99 99)
           (.drawLine 0 0 99 99))
   :player (image-icon-pad [100 100] 
                      {:questions [{:id      :direction 
                                    :type    :direction 
                                    :label   "Enter Direction"}]}
             (.drawString  "☺" 49 49))
   :blank  default-image})

(def widget-templates
  [function-cannon-template])


(defn template->item-icon [template]
  (let [{:keys [build questions image]} template]
    (image-icon* image {:questions questions :build build})))

(defn item-panel []
 (listbox :model (concat (vals default-item-icons)
                         (map template->item-icon widget-templates))))


(defn update-selected! [item-box e]
  (config! (.getComponent e)
      :icon (let [icon (selection item-box)
                  questions (:questions icon)]
              (if questions
                (image-icon* (.getImage icon) (apply get-values questions))
                (image-icon* (.getImage icon) {})))))

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
                     (:direction (config c :icon))]
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
                                             build (:build icon)
                                             questions (:questions icon)]
                                       :when build]
                 (build (apply get-values questions))))]
    {:loc loc
     :dir dir
     :walls walls
     :wall-mat "Textures/Terrain/BrickWall/BrickWall.jpg"
     :widgets widgets}))
     

(defn view-button [map-panel]
  (action :name "View"
          :handler
          (fn [_]
            (view-level (code map-panel)))))

(defn build-button [map-panel output-panel]
  (action :name "Build"
          :handler 
          (fn [_]
            (config! output-panel :text
                     (str (code map-panel))))))



(defn make-gui []
  (let [item-box (item-panel)
        map-panel  (map-panel item-box)
        output-panel (output-panel map-panel)
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
                                         :items [build-button
                                                  view-button])))))

(defn -main [& args]
  (-> (make-gui)
      (show!)))
