(ns jme-clj.audio
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.audio AudioRenderer]))

(defprotocol AudioRendererOwner
  (audio-renderer [owner]))

(extend-type SimpleApplication
  AudioRendererOwner
  (audio-renderer [app] (.getAudioRenderer app)))

(extend AudioRenderer
  AudioRendererOwner
  {:audio-renderer identity})

