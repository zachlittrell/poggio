(ns nifty-clj.textfieldx
  "Functions for creating and manipulating a textfieldx, a special
  nifty text area."
  (:require [clojure.string :as str])
  (:use [data string]
        [nifty-clj [builders :exclude [text]] events elements]))

(defn textfield-entry 
  "Creates a textfield-entry with line-number, line, and an optional 
   tail textfield entry (which must be a builder)."
  ([scroll-panel-id line-number line]
   (textfield-entry scroll-panel-id line-number line nil))
  ([scroll-panel-id line-number line tail]
   (panel :child-layout :vertical
          :width "100%"
          :panels  (cons (panel :child-layout :horizontal
                                :width "100%"
                                :controls [(label :text (str line-number))
                                           (text-field :initial-text line
                                                       :on-get-focus-effect 
                                                       (effect
                                                         :effect-name 
                                                         "updateScrollpanelPositionToDisplayElement"
                                                         :effect-parameters
                                                         {"target"
                                                          scroll-panel-id})

                                                       :font "Interface/Fonts/Monospaced.fnt" ;;I don't think this actually gives me a monospaced font...
                                                       :id (genstr "textbox")
                                                       :width "*")])
                         (if (nil? tail) nil (list tail))))))
  

(defn textfield
  "Returns the actual textfield."
  [textfield-entry]
  (-> textfield-entry
      (.getElements)
      (first)
      (.getElements)
      (second)))

(defn parent
  "Returns the previous element before textfield-entry."
  [textfield-entry]
  (.getParent textfield-entry))

(defn tail
  "Returns the next element."
  [textfield-entry]
  (let [elems (.getElements textfield-entry)]
    (when (== 2 (count elems))
      (second elems))))

(defn line-number
  "Returns the line-number."
  [textfield-entry]
  (-> textfield-entry
      (.getElements)
      first
      (.getElements)
      first
      text
      (Integer/parseInt)))


(defn set-line-number!
  "Sets the line-number to be (f (line-number textfield-entry))"
  [textfield-entry f]
  (let [label (-> textfield-entry
                  (.getElements)
                  first
                  (.getElements)
                  first)]
    (set-text! label (str (f (Integer/parseInt (text label)))))))

(defn first-entry? [textfield-entry]
  (== (line-number textfield-entry) 1))

(defn line-text
  "Returns the text of the entry."
  [textfield-entry]
  (-> textfield-entry
      (.getElements)
      (first)
      (.getElements)
      (second)
      (text-field-text)))
      

(defn scroll-to-entry! [textfield-entry]
  (let [line-number (line-number textfield-entry)]
    ))

(defmacro do-entries [[sym entry] & body]
  `(loop [~sym ~entry]
     (when (not (nil? ~sym))
       ~@body
       (recur (tail ~sym)))))

(defn set-tail! 
  ([entry new-tail]
   (set-tail! nil entry new-tail))
  ([nifty entry new-tail]
    (let [elems (.getElements entry)]
      (if (== 1 (count elems))
        (.add entry new-tail)
        (let [prev-tail (tail (second elems))]
          (remove-element! prev-tail)
          (if (element? new-tail)
            (.add entry new-tail)
            (build nifty entry new-tail)))))))

(declare swap-cursors!)
(defn remove-entry! [entry]
  ;;Do not allow them to remove the very first line
  (when (not (first-entry? entry))
    (let [parent (parent entry)
          next-tail (tail entry)]
      (swap-cursors! entry parent :end)
      (if (nil? next-tail)
        (.markForRemoval entry)
        (.markForMove next-tail parent
          (reify de.lessvoid.nifty.EndNotify
            (perform [_] 
              (do-entries [entry* next-tail]
                 (set-line-number! entry* dec))
              (.markForRemoval entry)))))
      (.layoutElements parent))))

(declare initialize-entry!)
(defn new-entry! [nifty scroll-panel-id  entry]
  (let [prev-tail (tail entry)
        new-entry (textfield-entry scroll-panel-id
                                   (inc (line-number entry)) "")]
    ;;(.markForRemoval prev-tail)
    (let [new-entry* (build nifty entry new-entry)]
      (initialize-entry! nifty scroll-panel-id new-entry*)
      (swap-cursors! entry new-entry*)
      (when prev-tail
        (.markForMove prev-tail new-entry*
              (reify de.lessvoid.nifty.EndNotify
                (perform [_] 
                  (do-entries [entry* (tail new-entry*)]
                      (set-line-number! entry* inc)))))))))

(defn lines [entry]
  "Returns the text of this entry and every subsequent
   entry, separated by \n."
  (let [buffer (StringBuilder.)]
    (do-entries [entry* entry]
      (.append buffer (line-text entry*))
      (.append buffer "\n"))
    (.substring buffer 0 (max 1 (dec (.length buffer))))))

(defn textfieldx-component [& {:keys [id text width height]
                               :or {id ""
                                    text ""
                                    width "100%"
                                    height "100%"}}]
  (let [lines (str/split-lines text)
        original-id id]
    (scroll-panel :id id
                 :width width
                 :height height
                :set {"horizontal" "false"}
                :style "nifty-listbox"
                :panels 
                [(panel :child-layout :vertical
                        :id "textfield-entries"
                        :width "100%"
                        :panel
                        (loop [prev nil
                               index (count lines)
                               lines (rseq lines)
                                ]
                          (if-let [[end & front] lines]
                            (let [field (textfield-entry original-id index end prev)]
                              (recur field
                                     (dec index)
                                     front))
                            prev)))])))

(defn first-entry
  "Returns the first entry of the textfieldx."
  [textfieldx]
  (-> textfieldx
      (select , "textfield-entries")
      (.getElements)
      (first)))

(defn parent-component 
  [entry]
  (first
    (drop (+ 3 (line-number entry))
          (iterate #(.getParent %) entry))))

;;(defn scroll-into-view! [entry]
;;  (let [component (parent-component entry)
;;        scroll-bar (.findNiftyControl component
;;                        "#nifty-internal-vertical-scrollbar"
;;                        de.lessvoid.nifty.controls.Scrollbar)
;;        min-y (int (.getValue scroll-bar))
;;        max-y (+ min-y (int (.getWorldPageSize scroll-bar)))
;;        current-min-y (+ (- (.getY entry) (.getY component))
;;                         min-y)
;;        current-max-y (+ (- (.getY entry) (.getY component))

(defn swap-cursors! 
  ([entry tail]
   (swap-cursors! entry tail nil))
  ([entry tail position]
  (let [entry-logic (text-field-logic  
                     (nifty-control
                        (textfield entry) :text-field))
       tail-field (textfield tail)
       tail-control (nifty-control tail-field
                                   :text-field)]
       (.setFocus tail-field)
       (.setCursorPosition tail-control
          (case position
            :end (.length (.getRealText tail-control))
            (.getCursorPosition entry-logic)))
   ))) ;   (scroll-into-view! tail))))

        ;;TODO
        ;;maybe write an accompanying de-initialize?
(defn initialize-entry! [nifty scroll-panel-id entry]
  (subscribe! nifty (.getId (textfield entry)) 
              :nifty-input-event
    (fn [topic data]
      (condp = (nifty-input-event->keyword data)
        :submit-text 
          (new-entry! nifty scroll-panel-id  entry)
        :backspace
          (when (= "" (line-text entry))
            (remove-entry! entry)) 
        :move-cursor-down
          (when-let [tail (tail entry)]
            (swap-cursors! entry tail))
        :move-cursor-up
           (when (not (first-entry? entry))
             (swap-cursors! entry (parent entry)))
        nil))))

(defn set-lines!
  "Sets the text of this textfieldx."
  [nifty textfieldx text]
  (let [[line & lines] (or (seq (str/split-lines text)) [""])
        scroll-panel-id (.getId textfieldx)
        entry (first-entry textfieldx)
        add-rest #(loop [lines lines
                         entry entry
                         index 2]
                    (when-let [[line & lines] (seq lines)]
                      (let [new-entry* (build nifty entry
                                             (textfield-entry 
                                               scroll-panel-id
                                               index
                                               line))]
                        (initialize-entry! nifty scroll-panel-id new-entry*)
                        (recur lines new-entry*
                               (inc index)))))]
    (.setText (nifty-control (textfield entry) :text-field) line)
    (if-let [t (tail entry)]
      (.markForRemoval t
        (reify de.lessvoid.nifty.EndNotify
          (perform [_] (add-rest))))
      (add-rest))))



(defn textfieldx
  "Returns a map, with :textfieldx mapping to the actual textfieldx,
   and :initialize! mapping to a function that takes a nifty instance
   and initializes this textfield."
  [& opts]
  (let [{:keys [id] :as opts-map} opts]
    {:textfieldx (apply textfieldx-component opts)
     :initialize! (fn [nifty]
                    (do-entries [entry (first-entry 
                                         (select 
                                           (select (.getCurrentScreen nifty) id)
                                           "textfield-entries"))]
                      (initialize-entry! nifty id entry)))}))
