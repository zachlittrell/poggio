(ns jme-clj.node
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.scene Node]))

(defprotocol Nodule
  (node [nodule]))

(extend-type SimpleApplication
  Nodule
  (node [app] (.getRootNode app)))

(extend Node
  Nodule
  {:node identity})
