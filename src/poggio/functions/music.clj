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

(def middle-c* (code-pog-fn []
                  (docstr [] "middle c for a quarter note")
                  "(note 60 0.25)"))

;;(def octave-up (code-pog-fn []
;;                  (docstr [["n" "a note"]]
;;                          "n one octave higher."))
