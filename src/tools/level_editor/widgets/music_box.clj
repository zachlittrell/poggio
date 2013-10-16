(ns tools.level-editor.widgets.music-box
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.collision.shapes SphereCollisionShape]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Sphere])
  (:require [clojure.string :as str]
            [tools.level-viewer.context :as context])
  (:use [control assert bindings timer]
        [data coll color object notes ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector spatial transform]
        [nifty-clj popup]
        [poggio.functions core parser modules scenegraph color utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(defn play-note! [app *stereos* on-error! player note]
  (let [[stereo & stereos] @*stereos*]
    (.stream player (note->music-string note))
    (when (seq stereo)
      (invoke* (spatial-pog-fn (select app stereo)) [on-error! (list note)]))
    (reset! *stereos* stereos)))

(defn wait-note 
  [note]
  (+ 0.01 (* 2.75 (duration note))))

(defn set-volume! [spatial mute?]
  (let [player (get-user-data! spatial "player")
        *muted?* (get-user-data! spatial "*muted?*")]
    (if mute?
      (when-not @*muted?*
        (.stream player mute-pattern)
        (reset! *muted?* true))
      (when @*muted?*
        (.stream player max-volume-pattern)
        (reset! *muted?* false)))))


(defn build-music-box [{:keys [x z id direction stereos app
                               on-error!
                                     ]}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       music-box (model :asset-manager app
                          :model-name "Models/MusicBox/gramophone.scene"
                          :name id
                          :local-translation loc
                          :local-rotation dir)
       player (org.jfugue.StreamingPlayer.)
       *muted?* (atom false)]
   (.stream player (pattern 
                     (org.jfugue.Instrument. org.jfugue.Instrument/GUITAR)))
   (context/add-end-level-watch! app #(.close player))
   (set-user-data! music-box "player" player)
   (set-user-data! music-box "*muted?*" *muted?*)
   (.scale music-box 8.0 8.0 8.0)
   (.addControl music-box control)
   (attach-pog-fn!* music-box
      (do-list-pog-fn :spatial music-box 
                      :app app
                      :on-value! (partial play-note! 
                                          app
                                          (atom (cycle stereos)) 
                                          on-error!
                                          player)
                      :handle-continuation? false
                      :init-wait-time 0.5
                      :wait-time wait-note
                      :queue? false;;queue? 
                      :transformer-id "";;transformer-id?
          :valid-input-type org.jfugue.Note 
          :queue-init []
          :param "notes"))
   music-box))


(def music-box-template
  {:image (image-pad [100 100]
           (.drawString "♫" 45 45))
   :questions [{:id :id        :type :string    :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :stereos   :type [:list
                                      :type :string] :label "Stereos"}]
   :build build-music-box
   })

