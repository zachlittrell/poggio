(ns nifty-clj.events
  "Functions for handling events (in particular, interactions) in nifty-gui."
  (:use [data.string :only [dash->camel-case]])
  (:import [clojure.lang IFn]
           [de.lessvoid.nifty NiftyMethodInvoker]))

(defn ifn-nifty-method-invoker
  "Returns a Nifty Method Invoker that calls function f on
   invocation."
  [f]
  (proxy [NiftyMethodInvoker] [nil]
    (invoke [_] (do (f) true))
    (performInvoke [_] (f))))

(defmacro nifty-method [& body]
  `(ifn-nifty-method-invoker
     (fn [_] ~@body)))

(defprotocol NiftyMethodInvokerProvider
  (nifty-method-invoker [this]))

(extend NiftyMethodInvoker
  NiftyMethodInvokerProvider
  {:nifty-method-invoker identity})

(extend-type IFn
  NiftyMethodInvokerProvider
  (nifty-method-invoker [f] (ifn-nifty-method-invoker f)))

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
                (~method-call (~click-handler element#)
                              (nifty-method-invoker invoker#)))))]
    `(do
       ~@setters)))

(def-element-interaction-click-handler-setters left .getPrimary)
(def-element-interaction-click-handler-setters right .getSecondary)
(def-element-interaction-click-handler-setters middle .getTertiary)

