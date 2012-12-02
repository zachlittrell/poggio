(ns jme-clj.node
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.scene Node]))

(defprotocol NodeProvider
  (node [nodule]))

(extend-type SimpleApplication
  NodeProvider
  (node [app] (.getRootNode app)))

(extend Node
  NodeProvider
  {:node identity})
