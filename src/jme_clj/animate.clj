(ns jme-clj.animate
  (:import [com.jme3.animation LoopMode]
           [com.jme3.cinematic MotionPath MotionPathListener]
           [com.jme3.cinematic.events MotionEvent]
           [com.jme3.math Vector3f])
  (:use [data vector]
        [jme-clj physics physics-control]))

(defprotocol MotionPathProvider
  (motion-path [p] "Returns a MotionPath."))

(extend-protocol MotionPathProvider
  MotionPath
  (motion-path [mp] mp)
  clojure.lang.Seqable
  (motion-path [path] 
    (let [motion-path (MotionPath.)]
      (doseq [point path]
        (.addWayPoint motion-path (vectorf point)))
      motion-path)))

(defprotocol MotionPathListenerProvider
  (motion-path-listener [p]))

(extend-protocol MotionPathListenerProvider
  MotionPathListener
  (motion-path-listener [mpl] mpl)
  clojure.lang.IFn
  (motion-path-listener [f]
    (reify MotionPathListener
      (onWayPointReach [_ motion-control way-point-index]
        (f motion-control way-point-index)))))

(defn follow-path! [spatial time path]
  "Sets the spatial to follow the path, which should implement the
   MotionPathProvider protocol, with time duration. Returns the
   MotionEvent object controlling teh animation."
  (doto (MotionEvent. spatial (motion-path path) time LoopMode/DontLoop)
    (.play)))


(defn visibility! [app geom show?]
  "Toggles the visibility of the geom."
  (if (and show? (not (.getParent geom)))
    (do 
      (if-let [ctrl (physics-control geom)]
        (.setEnabled ctrl true))
      (attach! app geom))
    (when (.getParent geom)
      (detach! geom))))