(ns poggio.functions.music
  (:use [data notes]
        [poggio.functions core parser utilities]))

(def note* (fn->pog-fn note "note"
              ["pitch" "duration"]
              (docstr [["pitch" "a number"]
                       ["duration" "a positive number"]]
                      "a note with the given pitch and duration.")))

(def rest* (fn->pog-fn rest-note "rest"
              ["duration"]
              (docstr [["duration" "a positive number"]]
                  "a rest for the given duration.")))

(def rest?* (fn->pog-fn rest? "rest?"
              ["n"]
              (docstr [["n" "a note"]]
                      "whether n is actually a rest.")))



(def pitch* (fn->pog-fn pitch "pitch"
              ["n"]
              (docstr [["n" "a note"]]
                      "n's pitch.")))

(def duration* (fn->pog-fn duration "duration"
                  ["n"]
                  (docstr [["n" "a note"]]
                    "n's duration.")))

(defmacro defnote 
  ([letter pitch]
   `(defnote ~letter ~pitch "middle"))
  ([letter pitch modifier]
    (let [name (name letter)
          var-name (symbol (str modifier "-" name "*"))
          str-name (str modifier  " " name)]
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
