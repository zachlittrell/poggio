(ns jme-clj.node
  "Functions for handling Nodes in JME"
  (:use [jme-clj spatial])
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

(def-spatial-constructor node*
  [:setter]
  {:name ""}
  `(Node. ~'name)
  {:name [:no-op]
   :children [:do-seq
              :replace [#"^(.*)$","attach-child"]]})
