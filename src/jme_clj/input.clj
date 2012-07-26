(ns jme-clj.input
  (:import [com.jme3.input InputManager]
           [com.jme3.input.controls ActionListener
                                    AnalogListener
                                    InputListener
                                    KeyTrigger
                                    MouseAxisTrigger
                                    MouseButtonTrigger
                                    Trigger]))

(defn add-listener
  "Attaches the listener to the given triggers, using a specific key."
  ([manager key listener & triggers]
    (doto manager
      (.addMapping key (into-array Trigger triggers))
      (.addListener listener
                    (doto (make-array String 1) (aset 0 key)))))) 

(def ^{:doc "The options used by trigger events for selecting what InputListener
             to use."}
  listener-options 
  {:action [`ActionListener 'onAction]
   :analog [`AnalogListener 'onAnalog]})

(defmacro def-trigger-event
  "Defines a macro with event as its name that takes an input-manager,
   an argument for each parameter in trigger-args, bindings to be
   passed to onAction in ActionListener, and a body of code that
   will be used in the onAction function."
  [event trigger & trigger-args]
  (let [event-name (str event)]
    `(defmacro ~event [input-manager# type# ~@trigger-args bindings# ~'& body#]
       (if-let [[listener-class# on-listener-call#] (listener-options type#)]
         `(add-listener ~input-manager#
            (str "jme-clj~" ~~event-name (clojure.lang.RT/nextID))
            (reify ~listener-class#
              (~on-listener-call# ~bindings#
                ~@body#))
            (new ~~trigger ~~@trigger-args))))))

(def-trigger-event on-key KeyTrigger key-code)
(def-trigger-event on-mouse-button MouseButtonTrigger mouse-button)
(def-trigger-event on-mouse-axis MouseAxisTrigger mouse-axis negative)
