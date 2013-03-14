(ns seesawx.core
  (:import [java.awt BasicStroke Color]
           [java.awt.image BufferedImage]
           [javax.swing ImageIcon JComponent])
  (:use [data.map :only [value-map]]
        [seesaw behave core value]))

(defmacro for-grid-panel [[[row column component] panel & opts] & body]
  "A for-loop that loops through each child of the grid panel, 
   binding the current row, column, and component to the given variables."
  `(let [grid# (.getLayout ~panel)
         max-x# (.getRows grid#)
         max-y# (.getColumns grid#)]
     (for [~row (range 0 max-x#)
           ~column (range 0 max-y#)
           :let [~component (.getComponent ~panel 
                                           (+ (* ~row max-y#) ~column))]
           ~@opts]
       ~@body)))

(defn some-in-grid-panel [pred grid-panel]
  "Returns a vector containing the row, column, and component of the first
   component that (pred component) returns true for, or nil."
  (when-let [[coord & _] 
             (seq (for-grid-panel [[row column component] grid-panel
                                   :when (pred component)]
                    [row column component]))]
    coord))
                                         


(defn image-icon* [img image-meta]
  "Returns an ImageIcon using image that can also be treated
   like a map, whose keys and values are determined by image-meta."
  (proxy [ImageIcon clojure.lang.ILookup] [img]
    (valAt [key] (image-meta key))))

(defmacro image-pad [[width height] image-meta & body]
  "Returns an image-icon* with width and height and the given image-meta,
   and applies body to the graphics of the image."
  `(let [img# (BufferedImage. ~width ~height BufferedImage/TYPE_INT_ARGB)]
          (doto (.createGraphics img#)
                   (.setColor Color/BLACK)
                   ~@body)
          (image-icon* img# ~image-meta)))


(defn wheel [& options]
  "Returns a wheel component with a directed arrow whose value is the angle
   the arrow points to."
  (let [angle (atom (/ Math/PI 2.0))]
    (doto
    (proxy [JComponent seesaw.value.Value][]
      (container?* [] false)
      (value* [] @angle)
      (value!* [this v] (swap! angle (constantly v)))
      (paintComponent [g]
        (.drawRect g 0 0 (dec (.getWidth this)) (dec (.getHeight this)))
        (let [diameter (- (min (.getWidth this)
                                 (.getHeight this)) 6)
              radius (/ diameter 2)
              center (+ 3 radius)]
        (.setStroke g (BasicStroke. 3 BasicStroke/CAP_ROUND
                                      BasicStroke/JOIN_BEVEL))
        (.rotate g (- @angle) center center)
        (.drawOval g 3 3 diameter diameter)
        (.drawLine g center center
                     diameter (+ 3 radius)))))
      (when-mouse-dragged :drag (fn [e [dx dy]]
                                  (let [this (.getSource e)
                                        diameter (- (min (.getWidth this)
                                                         (.getHeight this))
                                                    6)
                                        radius (/ diameter 2)
                                        center (+ 3 radius)]
                                  (swap! angle 
                                         (constantly (- (Math/atan2 
                                                          (- (.getY e)
                                                              center)
                                                          (- (.getX e)
                                                              center))))))

                                  (.repaint (.getSource e)))))))

(def keyword->widget
  {:direction wheel})

(defn get-values [& questions]
  "Creates a dialog which creates widgets for each pair in questions,
   where a pair consists of a string for a label and a keyword for the 
   type of value desired, and returns a map of the final values, or nil
   if the user cancels."
  (let [questions (partition 2 questions)
        panel (grid-panel :columns 2
                          :items (flatten (value-map questions 
                                                     (comp #(%) keyword->widget))))]
    (show! (dialog :option-type :ok-cancel
                   :content panel
                   :size [300 :by 300]
                   :success-fn (fn [_] (value panel))))))


