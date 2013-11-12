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
  (.stream player (note->music-string note))
  (let [[stereo & stereos] @*stereos*]
    (when-not (rest? note)
      (when (seq stereo)
        (invoke* (spatial-pog-fn (select app stereo)) [on-error! (list note)]))
      (reset! *stereos* stereos))))

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
                               transformer-id
                               queue?
                               queue-init
                               interactive?
                               muted?
                                     ]}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       music-box (model :asset-manager app
                          :model-name 
                            (if (or (false? interactive?)
                                    queue?)
                              "Models/MusicBox/gramophone_inactive.scene"
                              "Models/MusicBox/gramophone.scene")
                          :name id
                          :local-translation loc
                          :local-rotation dir)
       player (org.jfugue.StreamingPlayer.)
       *muted?* (atom false)
       *stereos* (atom nil)]
   (.stream player (pattern 
                     (org.jfugue.Instrument. org.jfugue.Instrument/GUITAR)))
   (context/add-end-level-watch! app #(.close player))
   (set-user-data! music-box "player" player)
   (set-user-data! music-box "*muted?*" *muted?*)
   (set-volume! music-box muted?)
   (.scale music-box 8.0 8.0 8.0)
   (.addControl music-box control)
   (attach-pog-fn!* music-box
      (do-list-pog-fn :spatial music-box 
                      :app app
                      :on-value! (partial play-note! 
                                          app
                                          *stereos*
                                          on-error!
                                          player)
                      :on-invoke! #(reset! *stereos* (cycle stereos))
                      :handle-continuation? false
                      :init-wait-time 0.5
                      :wait-time wait-note
                      :queue? queue? 
                      :interactive? (not (false? interactive?))
                      :transformer-id transformer-id
                      :on-error! on-error!
          :valid-input-type org.jfugue.Note 
          :queue-init queue-init
          :param "notes"))
   (start-do-list!? music-box queue? queue-init)
   music-box))


(def music-box-template
  {:image (image-pad [100 100]
           (.drawString "♫" 45 45))
   :questions [{:id :id        :type :string    :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :stereos   :type [:list
                                      :type :string] :label "Stereos"}
               {:id :muted?    :type :boolean   :label "Muted?"}
               {:id :transformer-id :type :string :label "Transformer"}
               {:id :queue? :type :boolean :label "Queue?"}
               {:id :queue-init :type [:string :multi-line? true]
                                :label "Queue Init"}
               {:id :interactive? :type [:boolean :init true]
                                  :label "Interactive?"}]
   :build build-music-box
   })

