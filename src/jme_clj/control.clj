(ns jme-clj.control
  (:use [control timer])
  (:import [com.jme3.scene.control Control]))

(defrecord JMEControlTimer [timer-control spatial]
  Timer
  (start! [this] (.addControl spatial timer-control))
  (stop! [this] (.removeControl spatial timer-control)))

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

(defn control-timer
  "Returns a Timer protocol object that when started,
   adds a timer control to the given spatial, and removes
   the control when stopped. One may either pass
   directly the timer control and spatial, or pass the spatial
   and arguments as per the function timer-control."
  ([timer-control spatial]
   (JMEControlTimer. timer-control spatial))
  ([spatial time f]
   (control-timer spatial time false f))
  ([spatial time repeat? f]
   (control-timer (timer-control time repeat? f) spatial)))

