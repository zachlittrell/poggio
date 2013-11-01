(ns jme-clj.control
  (:use [control timer]
        [data either])
  (:import [com.jme3.scene.control AbstractControl]))

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
    (let [time-elapsed (atom 0)]
      (proxy [AbstractControl][]
        (controlRender [rm vp])
        (controlUpdate [tpf]
          (let [now (swap! time-elapsed + tpf)]
            (when (>= now time)
              (let [result (f)]
                (if (and repeat? result)
                  (swap! time-elapsed (constantly 0))
                  (.removeControl (.getSpatial this) this))))))))))

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

(defn computation-control
  "Returns a control that computes f in a separate thread.
   If it times-out or throws an error, the fail function is called
   with the error message. Else, the success function is called
   with the result of f."
  [time-out f success fail]
  (let [computation (future (try-right f))
        time-elapsed (atom 0)]
    (proxy [AbstractControl][]
      (setSpatial [spatial]
        (proxy-super setSpatial spatial)
        ;;If we stop this timer, we want to 
        ;;cancel the computation.
        (when-not spatial
          (future-cancel computation)))
      (controlRender [rm vp])
      (controlUpdate [tpf]
        (let [now (swap! time-elapsed + tpf)]
          (if (realized? computation)
            (let [result @computation]
              (on-either result success fail)
              (.removeControl (.getSpatial this) this))
            (when (>= now time-out)
              (future-cancel computation)
              (fail (Exception. "Computation timed out."))
              (.removeControl (.getSpatial this) this))))))))

(defn computation-timer
  "Returns a Timer protocol object that when started,
   adds a computation control to the given spatial, and 
   removes the control when stopped. One may either pass
   directly the computation control and spatial, or pass the spatial
   and arguments as per the function computation-control."
  ([computation-control spatial]
   (JMEControlTimer. computation-control spatial))
  ([spatial time-out f success fail]
   (computation-timer (computation-control time-out f success fail)
                      spatial)))

(defn one-shot!
  "Attaches a control to spatial that runs f as soon as possible and then removes it."
  ([spatial f]
   (one-shot! spatial 0 f))
  ([spatial delay f]
    (-> (control-timer spatial delay f)
        (start!))))

(defmacro do-once!*
  "Wraps body in a function that is given to one-shot!"
  [spatial delay & body]
   `(one-shot! ~spatial ~delay (fn [] ~@body)))

(defmacro do-once!
  [spatial & body]
  `(do-once!* ~spatial 0 ~@body))
