(ns jme-clj.input
  (:import [com.jme3.input InputManager]
           [com.jme3.input.controls ActionListener
                                    InputListener
                                    KeyTrigger
                                    MouseAxisTrigger
                                    MouseButtonTrigger
                                    Trigger]))

(defn add-listener
  "Attaches the listener to the given triggers, using a specific key
   if supplied. Otherwise, the key is generated arbitrarily."
  ([manager key listener & triggers]
    (doto manager
      (.addMapping key (into-array Trigger triggers))
      (.addListener listener
                    (doto (make-array String 1) (aset 0 key)))))) 

(defmacro def-trigger-event
  "Defines a macro with event as its name that takes an input-manager,
   an argument for each parameter in trigger-args, bindings to be
   passed to onAction in ActionListener, and a body of code that
   will be used in the onAction function."
  [event trigger & trigger-args]
  (let [event-name (str event)]
    `(defmacro ~event [input-manager# ~@trigger-args bindings# ~'& body#]
       `(add-listener ~input-manager#
          (str "jme-clj~" ~~event-name (clojure.lang.RT/nextID))
          (reify ActionListener
            (onAction ~bindings#
              ~@body#))
          (new ~~trigger ~~@trigger-args)))))

(def-trigger-event on-key KeyTrigger key-code)
(def-trigger-event on-mouse-button MouseButtonTrigger mouse-button)
(def-trigger-event on-mouse-axis MouseAxisTrigger mouse-axis negative)
