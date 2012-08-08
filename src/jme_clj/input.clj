(ns jme-clj.input
  (:use [clojure.core.match :only [match]]
        [jme-clj.collision :only [closest-collision-from-camera]])
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

(def trigger-event->trigger
  {:on-key          KeyTrigger
   :on-mouse-button MouseButtonTrigger
   :on-mouse-axis   MouseAxisTrigger})

(def trigger-events
  {:on-key          `on-key
   :on-mouse-button `on-mouse-button
   :on-mouse-axis   `on-mouse-axis})

(defn on-select-action-code
  "Returns the code to be used by on-select-listener for
   handling different types of actions."
  [input-manager key action]
  (match (seq action)
    ([:on-key type key-code bindings & body] :seq)
      `(on-key ~input-manager ~type ~key ~key-code ~bindings ~@body)
    ([:on-mouse-button type mouse-button bindings & body] :seq)
      `(on-mouse-button ~input-manager ~type ~key ~mouse-button ~bindings ~@body)
    ([:on-mouse-axis type mouse-axis negative bindings & body] :seq)
      `(on-mouse-axis ~input-manager ~type ~key ~mouse-axis ~negative ~bindings ~@body)))

(defmacro on-select-listener[input-manager camera collidables name & actions]
  (let [id (clojure.lang.RT/nextID)
        mappings (map-indexed (fn [index [action-name & _]]
                                [(str "on-select-listener-"
                                      id
                                      "-" index)
                                 (trigger-event->trigger action-name)]) actions)
        manager (gensym "manager")
        add-actions (map-indexed
                      (fn [index action]
                        (on-select-action-code
                          manager
                          (str "on-select-listener-"
                                id
                                "-" index)
                          action))
                         actions)]
    `(let [~manager ~input-manager
           collidables# ~collidables
           camera# ~camera]
       (reify ActionListener
         (onAction [_ _ _ _]
           (doseq [[name# trigger#] ~mappings]
             (.deleteTrigger ~manager name# trigger#))
           (when-let [~name (closest-collision-from-camera
                              camera#
                              collidables#)]
             ~@add-actions))))))
