(ns tools.level-editor.widgets.model
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math FastMath ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str]
            [tools.level-editor.widgets.function-cannon :as cannon])
  (:use [control io timer]
        [data coll color notes object ring-buffer string quaternion]
        [jme-clj animate light geometry material model node physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))


(defn build-model [{:keys [x z id model-name direction collidable? x-delta y-delta z-delta app on-error!]}]
 (let [
       model (model :asset-manager app
                    :model-name model-name
                    :name id
                    :local-translation (Vector3f. (+ x-delta (* x 16)) 
                                                 (+ y-delta -16) 
                                                 (+ z-delta (* z 16)))
                    :local-rotation (angle->quaternion direction :y))]
   (when collidable?
     (.addControl model (RigidBodyControl. 0.0)))
   model))

(def model-template
  {:image (image-pad [100 100]
            (.drawString "♈" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :model-name :type :string :label "Model Name"}
               {:id :direction :type :direction :label "Direction"}
               {:id :x-delta :type :decimal :label "X"}
               {:id :y-delta :type :decimal :label "Y"}
               {:id :z-delta :type :decimal :label "Z"}
               {:id :collidable? :type :boolean :label "Collidable?"}]
   :build build-model
   })

