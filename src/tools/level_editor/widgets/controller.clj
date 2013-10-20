(ns tools.level-editor.widgets.controller
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str])
  (:use [control io timer]
        [data coll color object ring-buffer string quaternion]
        [jme-clj control node animate light geometry material model physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color modules parser utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))


(defn merge-imports [imports app]
  (loop [env core-env
         imports imports]
    (if (empty? imports)
      (let [[import & imports] imports]
        (recur (assoc env import (select app import))
               imports))
      env)))


(defn build-controller [{:keys [x  z id on-error! param imports code app]}]
  (let [pog-fn (code-pog-fn [param] "" code)
        node (node* :id id)]
    (attach-pog-fn!* node
        (fn->pog-fn 
          (fn [_ input]
           (start! (computation-timer node 5
                      #(invoke* pog-fn (merge-imports imports app) [input]))))
          ""
          ["on-error" param]
          ""))
    node))
                              

(def controller-template
  {:image (image-pad [100 100]
            (.drawString "&" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :imports :type [:list :type :string]
                             :label "Imports"}
               {:id :param :type :string :label "Param"}
               {:id :code :type [:string :multi-line? true]
                          :label "Code"}]
   :build build-controller
   })

