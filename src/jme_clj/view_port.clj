(ns jme-clj.view-port
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.renderer ViewPort]))

(defprotocol ViewPortOwner
  (view-port [owner]))

(extend-type SimpleApplication
  ViewPortOwner
  (view-port [app] (.getViewPort app)))

(extend ViewPort
  ViewPortOwner
  {:view-port identity})

