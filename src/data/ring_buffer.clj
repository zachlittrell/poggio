(ns data.ring-buffer
  (:use [data coll]))

(defn push [rb v]
  "Pushes a new value onto the ring-buffer."
  (let [{:keys [buffer cursor]} rb]
    (assoc rb :buffer (assoc buffer cursor v)
              :cursor (mod (inc cursor) (count buffer)))))

(defn lifo [rb]
  "Returns ring-buffer as a sequence in the LIFO order."
  (let [{:keys [buffer cursor]} rb]
    (map buffer (concat (range (dec cursor) -1 -1)
                        (range (dec (count buffer)) 
                               (dec cursor) -1)))))

(defrecord RingBuffer [buffer cursor]
  Adjoinable
  (adjoin [rb v] (push rb v)))

(defn ring-buffer [capacity]
  "Returns a map representing a ring buffer."
  (RingBuffer. (vec (repeat capacity nil)) 0))
