(ns poggio.functions.scenegraph
  (:import [com.jme3.scene Node])
  (:use [jme-clj spatial]
        [poggio.functions core]))

(defn pog-fn->node [name pog-fn]
  "Returns a Node that implements PogFn protocol, and delegates
   its implementation to pog-fn."
  (proxy [Node poggio.functions.core.PogFn] [name]
     (parameters [] (parameters pog-fn))
     (invoke [args] (invoke pog-fn args))
     (docstring [] (docstring pog-fn))))


(def-spatial-constructor pog-fn-node
  [:setter]
  {:name ""
   :pog-fn nil}
  `(pog-fn->node ~'name ~'pog-fn)
  {:name [:no-op]
   :pog-fn [:no-op]
   :children [:do-seq
              :replace [#"^(.*)$","attach-child"]]})

(defn pog-fn-node-from 
  ([spatial]
   (when spatial
     (if (is-pog-fn? spatial)
       spatial
       (recur (.getParent spatial)))))
  ([spatial arity]
   (when spatial
     (if (is-pog-fn? spatial arity)
       spatial
      (recur (.getParent spatial) arity)))))

