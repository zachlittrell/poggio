(ns data.notes
  "Functions for interacting with musical notes."
  (:import [org.jfugue JFugueElement Note Controller Pattern]))

;;TODO enforce limits on pitch and duration's limits
(defn note
  "Returns a note with given pitch or duration. Default duration
   is a quarter note."
  ([pitch]
   (note pitch 0.25))
  ([pitch duration]
   (Note. (byte pitch) (float duration))))

(defn rest-note
  "Returns a rest with given duration."
  [duration]
  (doto (Note. (byte 0) (float duration))
    (.setRest true)))

(defn rest?
  "Returns true if the note is actually a rest."
  [^Note n]
  (.isRest n))

(defn pitch 
  "Returns the pitch of the note."
  [^Note note]
  (.getValue note))

(defn duration
  "Returns the duration of the note."
  [^Note note]
  (.getDecimalDuration note))

(defn pattern 
  [& elements]
  (let [pattern (Pattern.)]
    (doseq [element elements]
      (.addElement pattern ^JFugueElement element))
    pattern))

(defn note->music-string
  "Returns a music-string representation for note."
  [^Note note]
  (str "[" (pitch note) "]"
       "/" (duration note)))

(defn- coarse-value [n]
  (byte (/ n 128)))

(defn- fine-value [n]
  (byte (mod n 128)))

(defn volume-pattern 
  "Returns a pattern containing controllers that sets the volume."
  [volume]
  (pattern (Controller. Controller/VOLUME_COARSE (coarse-value volume))
           (Controller. Controller/VOLUME_FINE (fine-value volume))))

(def mute-pattern
  "A pattern that mutes the volume."
  (volume-pattern 0))

(def max-volume-pattern
  "A pattern that turns the volume all the way up."
  (volume-pattern 16383))
