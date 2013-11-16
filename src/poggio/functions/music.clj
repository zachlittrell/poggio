(ns poggio.functions.music
  (:import [org.jfugue Note])
  (:use [control assert]
        [data notes]
        [poggio.functions core number parser utilities]))

(def note* (fn->pog-fn 
            (fn [pitch duration]
              (assert! (in-bounds? pitch 0 127))
              (assert! (integral? pitch))
              (assert! (in-bounds? duration 0.1 1.0))
              (note pitch duration))
             "note"
              [{:name "pitch"
                :type Number}
               {:name "duration"
                :type Number}]
              (docstr [["pitch" "a number"]
                       ["duration" "a positive number"]]
                      "a note with the given pitch and duration.")))

(def rest* (fn->pog-fn 
             (fn [duration]
               (assert! (in-bounds? duration 0.1 1.0))
               rest-note) "rest"
              [{:name "duration"
                :type Number}]
              (docstr [["duration" "a positive number"]]
                  "a rest for the given duration.")))

(def rest?* (fn->pog-fn rest? "rest?"
              [{:name "n"
                :type Note}]
              (docstr [["n" "a note"]]
                      "whether n is actually a rest.")))



(def pitch* (fn->pog-fn pitch "pitch"
              [{:name "n"
                :type Note}]
              (docstr [["n" "a note"]]
                      "n's pitch.")))

(def duration* (fn->pog-fn duration "duration"
                  [{:name "n"
                    :type Note}]
                  (docstr [["n" "a note"]]
                    "n's duration.")))

(defmacro defnote 
  ([letter pitch]
   `(defnote ~letter ~pitch ""))
  ([letter pitch modifier]
    (let [name (name letter)
          [var-modifier str-modifier] (if (empty? modifier) 
                                    ["" ""]
                                    [(str modifier "-")
                                     (str modifier " ")])
          var-name (symbol (str var-modifier name "*"))
          str-name (str str-modifier name)]
    `(def ~var-name (code-pog-fn []
                      (docstr [] (str ~str-name " for a quarter note"))
                      (str "(note " ~pitch " 0.25)"))))))

(defnote c 60)
(defnote d 62)
(defnote e 64)
(defnote f 65)
(defnote g 67)
(defnote a 69)
(defnote b 71)
(defnote c 72 "high")


;;(def octave-up (code-pog-fn []
;;                  (docstr [["n" "a note"]]
;;                          "n one octave higher."))
