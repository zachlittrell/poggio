(ns jme-clj.audio
  "Functions for handling audio in JME."
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.audio AudioRenderer]))

(defprotocol AudioRendererProvider
  (audio-renderer [owner]))

(extend-type SimpleApplication
  AudioRendererProvider
  (audio-renderer [app] (.getAudioRenderer app)))

(extend AudioRenderer
  AudioRendererProvider
  {:audio-renderer identity})

