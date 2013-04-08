(ns jme-clj.control
  (:import [com.jme3.scene.control Control]))

(defn timer-control 
  "Returns a control that executes f after time has elapsed,
   using the time per frames to calculate elapsed time.
   You can optionally specify for the timer to repeat.
   The timer will remove itself after executing if either
   repeat? is false or f returns false."
  ([time f]
   (timer-control time false f))
  ([time repeat? f]
    (let [time-elapsed (atom 0)
          spatial (atom nil)]
      (reify Control
        (cloneForSpatial [this spatial])
        (render [this rm vp])
        (setSpatial [this spatial*]
          (swap! spatial (constantly spatial*)))
        (update [this tpf]
          (let [now (swap! time-elapsed + tpf)]
            (when (>= now time)
              (let [result (f)]
                (if (and repeat? result)
                  (swap! time-elapsed (constantly 0))
                  (.removeControl @spatial this))))))))))

