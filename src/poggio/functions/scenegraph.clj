(ns poggio.functions.scenegraph
  (:import [com.jme3.scene Spatial Node])
  (:use [jme-clj selector spatial]
        [poggio.functions core]))

(def pog-fn-name-key "node-pog-fn-key")
(def pog-fn-key "node-pog-fn")


(defn attach-pog-fn! 
  ([spatial pog-fn]
   (attach-pog-fn! spatial pog-fn (.getName spatial)))
  ([spatial pog-fn name]
    (set-user-data! spatial pog-fn-name-key name)
    (set-user-data! spatial pog-fn-key pog-fn)))

(defn attach-pog-fn!* 
  ([spatial pog-fn]
   (attach-pog-fn!* spatial pog-fn (.getName spatial)))
  ([^Spatial spatial pog-fn name]
    (do-dfs [spatial* spatial]
      (attach-pog-fn! spatial* pog-fn name))))

(defn pog-fn-name [spatial]
  (get-user-data! spatial pog-fn-name-key))

(defn spatial-pog-fn [spatial]
  (get-user-data! spatial pog-fn-key))

(defn is-pog-fn-spatial? 
  ([spatial arity]
   (when-let [f (spatial-pog-fn spatial)]
     (== arity (count (parameters f)))))
  ([spatial]
    (has-user-data? spatial pog-fn-key)))

(defn spatial-pog-fn-from [node id]
  (-> (select node id)
      (spatial-pog-fn)))

(defn pog-fn->node
  "Returns a Node that implements PogFn protocol, and delegates
   its implementation to pog-fn."
  [name pog-fn]
  ;;TODO This will be replaced with using getUserData
  (proxy [Node poggio.functions.core.PogFn poggio.functions.core.LazyPogFn] 
    [name]
     (parameters [] (parameters pog-fn))
     (lazy_invoke [env args]
          (invoke* pog-fn env args))
     (invoke [args] 
             (invoke* pog-fn {} args))
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

