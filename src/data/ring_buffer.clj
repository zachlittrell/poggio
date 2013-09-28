(ns data.ring-buffer
  (:use [data coll]))

(defn push
  "Pushes a new value onto the ring-buffer."
  [rb v]
  (let [{:keys [buffer cursor]} rb]
    (assoc rb :buffer (assoc buffer cursor v)
              :cursor (mod (inc cursor) (count buffer)))))

(defn lifo
  "Returns ring-buffer as a sequence in the LIFO order."
  [rb]
  (let [{:keys [buffer cursor]} rb]
    (map buffer (concat (range (dec cursor) -1 -1)
                        (range (dec (count buffer)) 
                               (dec cursor) -1)))))

(defrecord RingBuffer [buffer cursor]
  Adjoinable
  (adjoin [rb v] (push rb v)))

(defn ring-buffer
  "Returns a map representing a ring buffer."
  [capacity]
  (RingBuffer. (vec (repeat capacity nil)) 0))
