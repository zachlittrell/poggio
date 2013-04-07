(ns jme-clj.control
  (:import [com.jme3.scene.control Control]))

(defn alarm-control [alarm-time f]
  "Returns a control that executes f
   when alarm-time has passed."
  (let [time-elapsed (atom 0)
        spatial (atom nil)]
    (reify Control
      (cloneForSpatial [this spatial])
      (render [this rm vp])
      (setSpatial [this spatial*]
        (swap! spatial (constantly spatial*)))
      (update [this tpf]
        (let [now (swap! time-elapsed + tpf)]
          (when (>= now alarm-time)
            (.removeControl @spatial this)
            (f)))))))

