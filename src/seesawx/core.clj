(ns seesawx.core
  (:import [java.awt BasicStroke Color Graphics RenderingHints]
           [java.awt.image BufferedImage]
           [javax.swing ImageIcon JColorChooser JComponent])
  (:use [control.bindings :only [let-weave]]
        [data.map :only [value-map]]
        [seesaw behave core [selector :only [id-of!]] layout swingx value]))

(declare keyword->widget)

(defn get-grid-component [grid-panel row column]
  (let [grid (.getLayout grid-panel)]
    (.getComponent grid-panel (+ (* row (.getColumns grid)) column))))

(defmacro for-grid-panel
  "A for-loop that loops through each child of the grid panel, 
   binding the current row, column, and component to the given variables."
  [[[row column component] panel & opts] & body]
  `(let [grid# (.getLayout ~panel)
         max-x# (.getRows grid#)
         max-y# (.getColumns grid#)]
     (for [~row (range 0 max-x#)
           ~column (range 0 max-y#)
           :let [~component (.getComponent ~panel 
                                           (+ (* ~row max-y#) ~column))]
           ~@opts]
       ~@body)))

(defn some-in-grid-panel
  "Returns a vector containing the row, column, and component of the first
   component that (pred component) returns true for, or nil."
  [pred grid-panel]
  (when-let [[coord & _] 
             (seq (for-grid-panel [[row column component] grid-panel
                                   :when (pred component)]
                    [row column component]))]
    coord))
                                         


(defn image-icon*
  "Returns an ImageIcon using image that can also be treated
   like a map, whose keys and values are determined by image-meta."
  [img image-meta]
  (proxy [ImageIcon clojure.lang.IMeta] [img]
    (meta [] image-meta)))

(defmacro image-pad
  "Returns an image with width and height
1d   and applies body to the graphics of the image."
  [[width height] & body]
  `(let [img# (BufferedImage. ~width ~height BufferedImage/TYPE_INT_ARGB)]
          (doto (.createGraphics img#)
                   (.setColor Color/BLACK)
                   ~@body)
          img#))

(defmacro image-icon-pad
  "Returns an image-icon* with width and height and the given image-meta,
   and applies body to the graphics of the image."
  [[width height] image-meta & body]
  `(image-icon* (image-pad [~width ~height] ~@body) ~image-meta))

(defmacro image-icon-pad* [size image-meta & body]
  `(let [img# (image-pad ~size ~@body)]
     (image-icon* img# (assoc ~image-meta :image img#))))

(defn- wheel-drag [angle-atom e [dx dy]]
  (let [this (.getSource e)
        width (.getWidth this)
        height (.getHeight this)
        center-x (+ 3 (/ (- width 6) 2.0))
        center-y (+ 3 (/ (- height 6) 2.0))]
  (swap! angle-atom
         (constantly (- (Math/atan2 
                          (- (.getY e)
                              center-y)
                          (- (.getX e)
                              center-x))))))
  (.repaint (.getSource e)))

(defn wheel
  "Returns a wheel component with a directed arrow whose value is the angle
   the arrow points to."
  [& options]
  (let [{:keys [id init]
         :or {init (/ Math/PI 2.0)}} options
        angle (atom init)]
    (doto
    (proxy [JComponent seesaw.value.Value][]
      (container_QMARK__STAR_ [] false)
      (value_STAR_ [] @angle)
      (value!* [v] (swap! angle (constantly v)))
      (paintComponent [^Graphics g]
        (let [width (.getWidth this)
              height (.getHeight this)
              diameter (- (min width height) 6)
              radius (/ diameter 2)
              center-x (+ 3 (/ (- width 6) 2))
              center-y (+ 3 (/ (- height 6) 2))]
          (.setRenderingHint g RenderingHints/KEY_ANTIALIASING
                                RenderingHints/VALUE_ANTIALIAS_ON)
          (.setStroke g (BasicStroke. 3 BasicStroke/CAP_ROUND
                                        BasicStroke/JOIN_BEVEL))
          (.drawOval g (- center-x radius) (- center-y radius)
                       diameter diameter)
          (.rotate g (- @angle) center-x center-y)
          (.drawLine g center-x center-y
                       (+ center-x radius)  center-y))))
        (id-of! , id)
      (when-mouse-dragged :start #(wheel-drag angle % [0 0])
                          :drag (partial wheel-drag angle)))))

(extend-type org.jdesktop.swingx.JXColorSelectionButton
  Value
  (container?* [this] false)
  (value* [this] (let [c (selection this)]
                   {:red (.getRed c) 
                    :green (.getGreen c)
                    :blue (.getBlue c)}))
  (value!* [this v] (selection! this v)))

(defn listx
  "Creates a widget that allows the user to build up a list.
   Accepts a type option, which can be one of the widget keywords
   used by get-values (the :string type is used if omitted)."
  [& opts]
  (let [{:keys [type id init]
         :or
         {type :string
          init []}} opts
        widget (keyword->widget type)
        grid  (proxy [javax.swing.JPanel seesaw.value.Value] []
                (container_QMARK__STAR_ [] false)
                (value_STAR_ [] 
                  (vec
                    (for-grid-panel [[r c component] this
                                     :when (zero? c)]
                      (value component)))))
        revalidate! (fn [] (.setRows (.getLayout grid) 
                                     (/ (.getComponentCount grid) 2))
                           (.setSize grid (.getPreferredSize grid))
                           (.validate grid)
                           (.repaint grid))
        remove (fn [w]
                 (action :name "Remove"
                         :handler (fn [e]
                                    (remove! grid w 
                                             (.getSource e))
                                    (revalidate!))))]
    (id-of! grid id)
    (.setLayout grid (grid-layout 0 2))
    (doseq [item init]
      (let [w  (widget :init item)]
        (add! grid w (remove w))))
    (.setRows (.getLayout grid) (/ (count init) 2))
    (border-panel :center (scrollable grid)
                  :size [30 :by 200]
                  :north (action :name "Add"
                                 :handler
                                 (fn [e]
                                   (let [w (widget)]
                                     (add! grid
                                           w
                                           (remove w))
                                     (revalidate!)))))))

(defn- add-init! [f init]
  (fn [& opts]
    (let [{:as opts-map} opts]
      (if (contains? opts-map :init)
        (let [args (apply concat (seq (dissoc opts-map :init)))]
          (if (keyword? init)
            (apply f init (:init opts-map) args)
            (doto (apply f args)
              (init (:init opts-map)))))
        (apply f opts)))))

(def keyword->widget
  {:direction wheel
   :string    (add-init! text text!)
   :boolean   (add-init! checkbox :selected?)
   :color     (add-init! color-selection-button :selection)
   :choice    (add-init! combobox selection!)
   :list      listx
   :integer   (fn [& {:keys [id init] :or {init 0} }]
                (spinner :id id :model (spinner-model init :by 1)))
   :decimal   (fn [& {:keys [id init] :or {init 0.0}}]
                (apply spinner :id id :model (spinner-model init :by 0.1)
                         []))})

(defn get-values
  "Creates a dialog which creates widgets for each pair in questions,
   where a pair consists of a string for a label and a keyword for the 
   type of value desired, and returns a map of the final values, or nil
   if the user cancels."
  [& questions]
  (let [panel (grid-panel :columns 2
                          :items (flatten 
                                   (for [{:keys [id type label]} questions]
                                    [label 
                                     (let-weave (coll? type)
                                        [[type & opts] type]
                                        [opts nil]
                                     (apply (keyword->widget type)
                                            :id id
                                            opts))])))]
    (show! (dialog :option-type :ok-cancel
                   :content (scrollable panel)
                   :size [400 :by 400]
                   :success-fn (fn [_] (value panel))))))

