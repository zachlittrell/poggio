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
  `(defmacro ~event [input-manager# type# key# ~@trigger-args bindings# ~'& body#]
     (let [[listener-class# on-listener-call#] (listener-options type#)]
       `(add-listener ~input-manager#
                      ~key# 
                     (reify ~listener-class#
                       (~on-listener-call# ~bindings#
                         ~@body#))
                     (new ~~trigger ~~@trigger-args)))))

(defmacro def-trigger-event*
  "Defines the macro made by def-trigger-event, along with
   a helper function, called event*, that supplies an arbitrary
   value for the event handler key."
  [event trigger & trigger-args]
  (let [event-name (str event)
        starred-name (symbol (str event-name "*"))]
    `(let [event# (def-trigger-event ~event ~trigger ~@trigger-args)]
       (defmacro ~starred-name [input-manager# type# ~@trigger-args bindings# ~'& body#]
         `(~event# ~input-manager#
                   ~type#
                   (str "jme-clj-" ~~event-name "-"
                        (clojure.lang.RT/nextID))
                   ~~@trigger-args
                   ~bindings#
                   ~@body#)))))

(def-trigger-event* on-key KeyTrigger key-code)
(def-trigger-event* on-mouse-button MouseButtonTrigger mouse-button)
(def-trigger-event* on-mouse-axis MouseAxisTrigger mouse-axis negative)

(def trigger-event-to-trigger
  {:on-key          KeyTrigger
   :on-mouse-button MouseButtonTrigger
   :on-mouse-axis   MouseAxisTrigger})

(def trigger-events
  {:on-key          `on-key
   :on-mouse-button `on-mouse-button
   :on-mouse-axis   `on-mouse-axis})


