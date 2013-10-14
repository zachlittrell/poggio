(ns jme-clj.audio
  "Functions for handling audio in JME."
  (:use [jme-clj assets spatial])
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.audio AudioNode AudioRenderer]))

(defprotocol AudioRendererProvider
  (audio-renderer [owner]))

(extend-type SimpleApplication
  AudioRendererProvider
  (audio-renderer [app] (.getAudioRenderer app)))

(extend AudioRenderer
  AudioRendererProvider
  {:audio-renderer identity})

(def-spatial-constructor audio-node
  [:setter]
  {:asset-manager nil
   :name nil
   :buffered? true}
  `(AudioNode. (asset-manager ~'asset-manager) ~'name ~'buffered?)
  {:asset-manager [:no-op]
   :name [:no-op]
   :buffered? [:no-op]
   :looping? [:setter
              :replace [#"^(.*)\?$","$1"]]
   :positional? [:setter
                 :replace [#"^(.*)\?$","$1"]]})

(defrecord SoundtrackDJ [asset-manager 
                         *current-audio-node*
                         *current-set-list*]
  com.jme3.app.state.AppState
  (cleanup [this])
  (initialize [this state-manager app])
  (isEnabled [this] true)
  (isInitialized [this] true)
  (postRender [this])
  (render [this render-manager])
  (setEnabled [this enabled?])
  (stateAttached [this state-manager])
  (stateDetached [this state-manager])
  (update [this tpf]
    (let [current-audio-node ^AudioNode @*current-audio-node*
          set-list @*current-set-list*]
      (when (and (not-empty set-list)
                 (or (not current-audio-node) 
                     (= com.jme3.audio.AudioNode$Status/Stopped
                         (.getStatus current-audio-node))))
        (let [[current & more] set-list
              current-audio-node (reset! *current-audio-node*
                                 (audio-node :asset-manager asset-manager
                                             :name current
                                             :buffered? true
                                             :positional? false))]
          (reset! *current-set-list* more)
          (.play current-audio-node))))))

(defn soundtrack-dj [asset-manager]
  (SoundtrackDJ. asset-manager (atom nil) (atom nil)))

(defprotocol DJ
  (play! [dj set-list])
  (stop! [dj]))

(extend-protocol DJ
  SimpleApplication
  (play! [app set-list]
    (-> app
        (.getStateManager)
        (.getState SoundtrackDJ)
        (play! , set-list)))
  (stop! [app]
    (-> app
        (.getStateManager)
        (.getState SoundtrackDJ)
        (stop!)))
  SoundtrackDJ
  (play! [dj set-list]
    (stop! dj)
    (reset! (:*current-set-list* dj) (cycle set-list)))
  (stop! [dj]
    (reset! (:*current-set-list* dj) nil)
    (reset! (:*current-audio-node* dj) nil)))

