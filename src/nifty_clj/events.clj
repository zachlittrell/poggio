(ns nifty-clj.events
  "Functions for handling events (in particular, interactions) in nifty-gui."
  (:use [data.string :only [dash->camel-case]])
  (:import [clojure.lang IFn]
           [de.lessvoid.nifty NiftyMethodInvoker]
           [de.lessvoid.nifty.controls Droppable
                                       AbstractController 
                                       DefaultController 
                                       DroppableDropFilter]
           [de.lessvoid.nifty.screen DefaultScreenController
                                     NullScreenController]
           [org.bushe.swing.event EventTopicSubscriber]))

(defn ifn->nifty-method-invoker
  "Returns a Nifty Method Invoker that calls function f on
   invocation."
  [f]
  (proxy [NiftyMethodInvoker] [nil]
    (invoke [_] (do (f) true))
    (performInvoke [_] (f))))

(defmacro nifty-method [& body]
  `(ifn->nifty-method-invoker
     (fn [_] ~@body)))

(defprotocol NiftyMethodInvokerProvider
  (nifty-method-invoker [this]))

(extend NiftyMethodInvoker
  NiftyMethodInvokerProvider
  {:nifty-method-invoker identity})

(extend-type IFn
  NiftyMethodInvokerProvider
  (nifty-method-invoker [f] (ifn->nifty-method-invoker f)))

(defn set-on-mouse-over! [element invoker]
  (.setOnMouseOver (.getElementInteraction element)
                   (nifty-method-invoker invoker)))

(defn set-on-mouse-wheel! [element invoker]
  (.setOnMouseWheelMethod (.getElementInteraction element)
                          (nifty-method-invoker invoker)))

(def element-interaction-click-methods
  [:on-click :on-click-mouse-move :on-release])

(defmacro def-element-interaction-click-handler-setters
  [click-name click-handler]
  (let [setters 
         (for [method element-interaction-click-methods]
           (let [method-str (name method)
                 method-name (symbol (str "set-" click-name
                                          "-" method-str "!"))
                 method-call (symbol (str ".set"
                                          (dash->camel-case 
                                             method-str
                                             true)
                                         "Method"))]
             `(defn ~method-name [element# invoker#]
                (~method-call (~click-handler (.getElementInteraction element#))
                              (nifty-method-invoker invoker#)))))]
    `(do
       ~@setters)))

(def-element-interaction-click-handler-setters left .getPrimary)
(def-element-interaction-click-handler-setters right .getSecondary)
(def-element-interaction-click-handler-setters middle .getTertiary)

(defprotocol DroppableDropFilterProvider
  (droppable-drop-filter [p]))
(extend-protocol DroppableDropFilterProvider
  DroppableDropFilter
  (droppable-drop-filter [d] d)
  IFn
  (droppable-drop-filter [f] (proxy [NullScreenController DroppableDropFilter] []
                               (accept [source draggable target]
                                  (f source draggable target)))))

(defn add-filter! [droppable filter]
  (.addFilter droppable (droppable-drop-filter filter)))

(def element-interactions 
  {:on-mouse-over  set-on-mouse-over!
   :on-mouse-wheel set-on-mouse-wheel!
   :on-left-click       set-left-on-click!
   :on-left-click-mouse-move set-left-on-click-mouse-move!
   :on-left-release     set-left-on-release!
   :on-right-click       set-right-on-click!
   :on-right-click-mouse-move set-right-on-click-mouse-move!
   :on-right-release     set-right-on-release!
   :on-middle-click       set-middle-on-click!
   :on-middle-click-mouse-move set-middle-on-click-mouse-move!
   :on-middle-release     set-middle-on-release!})

(def keyword->nifty-control-class
  {:droppable Droppable})

(def nifty-control-interactions
  {:on-drop add-filter!})

(defprotocol EventTopicSubscriberProvider
  (event-topic-subscriber [e]))

(extend-protocol EventTopicSubscriberProvider
  EventTopicSubscriber
  (event-topic-subscriber [e] e)
  clojure.lang.IFn
  (event-topic-subscriber [f] (reify EventTopicSubscriber
                                (onEvent [_ topic data] (f topic data)))))

(defmulti nifty-event identity)
(defmethod nifty-event :default [e] e)
(defmethod nifty-event :drag [k]
  de.lessvoid.nifty.controls.DraggableDragStartedEvent)
(defmethod nifty-event :drop [k]
  de.lessvoid.nifty.controls.DroppableDroppedEvent)
(defmethod nifty-event :drop-down-select [k]
   de.lessvoid.nifty.controls.DropDownSelectionChangedEvent)
(defmethod nifty-event :text-field-change [k]
  de.lessvoid.nifty.controls.TextFieldChangedEvent)

(defn subscribe! 
  ([nifty id class subscriber]
   (subscribe! nifty (.getCurrentScreen nifty) id class subscriber))
  ([nifty screen id class subscriber]
   (.subscribe nifty screen id (if (keyword? class)
                                 (nifty-event class)
                                 class)
               (event-topic-subscriber subscriber))))

(defn unsubscribe! [nifty id obj]
  (.unsubscribe nifty id obj))

(defn unsubscribe-all!
  ([nifty]
   (unsubscribe-all! nifty (.getCurrentScreen nifty)))
  ([nifty screen]
   (.unsubscribe nifty screen)))

