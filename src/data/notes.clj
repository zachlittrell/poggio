(ns data.notes
  "Functions for interacting with musical notes.")

(defrecord Note [pitch duration rest?])

(defn note
  "Returns a note with given pitch or duration. Default duration
   is a quarter note."
  ([pitch]
   (note pitch 0.25))
  ([pitch duration]
   (Note. (byte pitch) (float duration) false)))

(defn rest-note
  "Returns a rest with given duration."
  [duration]
  (Note. (byte 0) (float duration) true))

(defn rest?
  "Returns true if the note is actually a rest."
  [^Note n]
  (:rest? n))


(defn pitch 
  "Returns the pitch of the note."
  [^Note note]
  (:pitch note))

(defn duration
  "Returns the duration of the note."
  [^Note note]
  (:duration note))

(defn note->pattern
  [^Note note]
  (str "[" (pitch note) "]/" (duration note)))
