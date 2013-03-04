(ns tools.level-editor.core
  (:import [java.awt Color]
           [java.awt.image BufferedImage])
  (:use [seesaw core]))

(def max-x 13)
(def max-y 13)

(defmacro image-pad [[width height] & body]
  `(let [img# (BufferedImage. ~width ~height BufferedImage/TYPE_INT_ARGB)]
     (doto (.createGraphics img#)
       (.setColor Color/BLACK)
       ~@body)
     (icon img#)))

(def default-image (BufferedImage. 100 100 BufferedImage/TYPE_INT_ARGB))

(def item-icons
  {:wall (image-pad [100 100]
           (.drawRect 0 0 99 99)
           (.drawLine 0 0 99 99))
   :player (image-pad [100 100]
             (.drawString  "☺" 49 49))})

(defn item-panel []
  (let [l-box (listbox :model (vals item-icons))]
    l-box))


(defn update-selected! [item-box e]
  (config! (.getComponent e)
      :icon (selection item-box)))

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

(defn build-button [map-panel output-panel]
  (action :name "Build"
          :handler 
          (fn [_]
            (config! output-panel :text
              (format basic-level-code-template
                (if-let [[p & _]
                         (let [player (:player item-icons)]
                             (seq (for [row (range 0 max-x)
                                        column (range 0 max-y)
                                        :let [component (.getComponent map-panel
                                                                       (+ (* row
                                                                             max-y)
                                                                          column))]
                                        :when (identical? player
                                                          (config component :icon))]
                                    [column row])))]
                  p
                  "[0 0]")
                (into #{}
                  (let [wall (:wall item-icons)]
                    (println wall)
                    (for [row (range 0 max-x)
                          column (range 0 max-y)
                          :let [component (.getComponent map-panel
                                                         (+ (* row max-y)
                                                            column))]
                          :when (identical? wall
                                            (config component :icon))]
                         [column row]))))))))



(defn make-gui []
  (let [item-box (item-panel)
        map-panel  (map-panel item-box)
        output-panel (output-panel map-panel)
        build-button (build-button map-panel output-panel)]
  (frame :title "Poggio Level Editor"
         :on-close :exit
         :size [500 :by 500]
         :content (border-panel :west (scrollable item-box)
                                :center (scrollable map-panel)
                                :north build-button
                                :south output-panel))))

(defn -main [& args]
  (-> (make-gui)
      (show!)))
