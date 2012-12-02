(ns jme-clj.view-port
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.renderer ViewPort]))

(defprotocol ViewPortProvider
  (view-port [owner]))

(extend-type SimpleApplication
  ViewPortProvider
  (view-port [app] (.getViewPort app)))

(extend ViewPort
  ViewPortProvider
  {:view-port identity})

