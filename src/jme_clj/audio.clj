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
