(ns nifty-clj.textfieldx
  "Functions for creating and manipulating a textfieldx, a special
  nifty text area."
  (:require [clojure.string :as str])
  (:use [data string]
        [nifty-clj [builders :exclude [text]] events elements]))

(defn textfield-entry 
  ([line-number line]
   (textfield-entry line-number line nil))
  ([line-number line tail]
   (panel :child-layout :vertical
          :width "100%"
          :panels  (cons (panel :child-layout :horizontal
                                :width "100%"
                                :controls [(label :text (str line-number))
                                           (text-field :initial-text line
                                                       :id (genstr "textbox")
                                                       :width "100%")])
                         (if (nil? tail) nil (list tail))))))
  

(defn textfield [textfield-entry]
  (-> textfield-entry
      (.getElements)
      (first)
      (.getElements)
      (second)))
     
(defn parent [textfield-entry]
  (.getParent textfield-entry))

(defn tail [textfield-entry]
  (let [elems (.getElements textfield-entry)]
    (when (== 2 (count elems))
      (second elems))))

(defn line-number [textfield-entry]
  (-> textfield-entry
      (.getElements)
      first
      (.getElements)
      first
      text
      (Integer/parseInt)))

(defn set-line-number! [textfield-entry f]
  (let [label (-> textfield-entry
                  (.getElements)
                  first
                  (.getElements)
                  first)]
    (set-text! label (str (f (Integer/parseInt (text label)))))))

(defn line-text [textfield-entry]
  (-> textfield-entry
      (.getElements)
      (first)
      (.getElements)
      (second)
      (text-field-text)))
      

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
  (when (not (zero? (line-number entry)))
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
(defn new-entry! [nifty entry]
  (let [prev-tail (tail entry)
        new-entry (textfield-entry (inc (line-number entry)) "")]
    ;;(.markForRemoval prev-tail)
    (let [new-entry* (build nifty entry new-entry)]
      (initialize-entry! nifty new-entry*)
      (swap-cursors! entry new-entry*)
      (when prev-tail
        (.markForMove prev-tail new-entry*
              (reify de.lessvoid.nifty.EndNotify
                (perform [_] 
                  (do-entries [entry* (tail new-entry*)]
                      (set-line-number! entry* inc)))))))))

(defn lines [entry]
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
  (let [lines (str/split-lines text)]
    (scroll-panel :id id
                 :width width
                 :height height
                :style "nifty-listbox"
                :panels 
                [(panel :child-layout :vertical
                        :id "textfield-entries"
                        :width "100%"
                        :panel
                        (loop [prev nil
                               index (dec (count lines))
                               lines (rseq lines)
                                ]
                          (if-let [[end & front] lines]
                            (let [field (textfield-entry index end prev)]
                              (recur field
                                     (dec index)
                                     front))
                            prev)))])))

(defn first-entry [textfieldx]
  (-> textfieldx
      (select , "textfield-entries")
      (.getElements)
      (first)))

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
            (.getCursorPosition entry-logic))))))

(defn initialize-entry! [nifty entry]
  (subscribe! nifty (.getId (textfield entry)) 
              :nifty-input-event
    (fn [topic data]
      (condp = (nifty-input-event->keyword data)
        :submit-text 
          (new-entry! nifty entry)
        :backspace
          (when (= "" (line-text entry))
            (remove-entry! entry)) 
        :move-cursor-down
          (when-let [tail (tail entry)]
            (swap-cursors! entry tail))
        :move-cursor-up
           (when (not (zero? (line-number entry)))
             (swap-cursors! entry (parent entry)))
        nil))))

(defn set-lines! [nifty textfieldx text]
  (let [[line & lines] (str/split-lines text)
        entry (first-entry textfieldx)
        add-rest #(loop [lines lines
                         entry entry
                         index 1]
                    (when-let [[line & lines] (seq lines)]
                      (recur lines
                             (build nifty entry (textfield-entry 1 line))
                             (inc index))))]
    (.setText (nifty-control (textfield entry) :text-field) line)
    (if-let [t (tail entry)]
      (.markForRemoval t
        (reify de.lessvoid.nifty.EndNotify
          (perform [_] (add-rest))))
      (add-rest))))



(defn textfieldx [& opts]
  (let [{:keys [id] :as opts-map} opts]
    {:textfieldx (apply textfieldx-component opts)
     :initialize! (fn [nifty]
                    (do-entries [entry (first-entry 
                                         (select 
                                           (select (.getCurrentScreen nifty) id)
                                           "textfield-entries"))]
                      (initialize-entry! nifty entry)))}))
