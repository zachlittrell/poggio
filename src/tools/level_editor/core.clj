(ns tools.level-editor.core
  (:import [java.awt Color]
           [java.awt.image BufferedImage]
           [javax.swing ImageIcon])
  (:use [seesaw core]
        [seesawx core]
        [tools.level-viewer [core :only [view-level]]]))

(def max-x 13)
(def max-y 13)

(def default-image (image-pad [100 100] {}))

(def item-icons
  {:wall (image-pad [100 100] {}
           (.drawRect 0 0 99 99)
           (.drawLine 0 0 99 99))
   :player (image-pad [100 100] 
                      {:questions [{:id      :direction 
                                    :type    :direction 
                                    :label   "Enter Direction"}]}
             (.drawString  "☺" 49 49))
   :blank  default-image})

(defn template->item-icon [template]
  (let [{:keys [build-template questions img] template}]
    (image-icon* img {:questions questions :build build-template})))

(defn item-panel []
 (listbox :model (vals item-icons)))


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

(def basic-level-code-template
"(fn [asset-manager] (poggio.level/basic-level %s %s (jme-clj.material/textured-material asset-manager \"Textures/Terrain/BrickWall/BrickWall.jpg\")))")

(defn code [map-panel]
  (format basic-level-code-template
    (let [player (.getImage (:player item-icons))]
     (if-let [[row column c] (some-in-grid-panel 
                               #(identical? player (.getImage (config % :icon)))
                               map-panel)]
       {:location [column row]
        :direction (:direction (config c :icon))}
       (throw (Exception. "Need to position player."))))
     (into #{}
       (let [wall (.getImage (:wall item-icons))]
         (for-grid-panel [[row column component] map-panel
                          :when (identical? wall (.getImage (config component :icon)))]
            [column row])))
      (into []
        (for-grid-panel [[row column component] map-panel
                         :let [icon (config component :icon)
                               build (:build icon)
                               questions (:questions icon)]
                         :when build]
          (build (apply get-values questions))))))

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
                     (code map-panel)))))



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
