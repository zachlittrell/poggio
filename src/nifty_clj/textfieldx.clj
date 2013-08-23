(ns nifty-clj.textfieldx
  "Functions for creating and manipulating a textfieldx, a special
  nifty text area."
  (:require [clojure.string :as str])
  (:use
        [nifty-clj [builders :exclude [text]] elements]))


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
      first
      text
      int))

(defn set-line-numer! [textfield-entry f]
  (let [label (-> textfield-entry
                  (.getElements)
                  first
                  first)]
    (set-text! label (str (f (int (text label)))))))

(defn line-text [textfield-entry]
  (-> textfield-entry
      (.getElements)
      (first)
      (second)
      (text)))
      

(defmacro do-entries [[sym entry] & body]
  `(loop [entry# ~entry]
     (when (not (nil? entry#))
       (let [~sym entry#]
         ~@body)
       (recur (tail entry#)))))

(defn set-tail! [entry new-tail]
  (let [elems (.getElements entry)]
    (if (== 1 (count elems))
      (.add entry new-tail)
      (let [prev-tail (tail (second elems))]
        (.markForRemoval prev-tail)
        (.add entry new-tail)))))

(defn remove-entry! [entry]
  ;;Do not allow them to remove the very first line
  (when (not (zero? (line-number entry)))
    (let [parent (parent entry)
          next-tail (tail entry)]
      (set-tail! parent next-tail)
      (do-entries [entry* next-tail]
        (set-line-number! entry* dec)))))

(defn new-entry! [entry]
  (let [prev-tail (tail entry)
        new-entry (textfield-entry (line-number entry) "" prev-tail)]
    (set-tail! entry new-entry)
    (do-entries [entry* new-entry]
      (set-line-number! entry* inc))))


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
                                                       :width "100%")])
                         (if (nil? tail) nil (list tail))))))

(defn textfieldx-component [& {:keys [id text width height]
                               :or {:id ""
                                    :text ""
                                    :width "100%"
                                    :height "100%"}}]
  (let [lines (str/split-lines text)]
    (scroll-panel :id id
                 :width width
                 :height height
                :style "nifty-listbox"
                :panels 
                [(panel :child-layout :vertical
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

