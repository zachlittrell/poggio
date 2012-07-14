(ns davel.core
  (:use [jme-clj [geometry :only [geom]]])
  (:require [jme-clj.input :as input])
  (:import (com.jme3.app SimpleApplication)
           (com.jme3.input KeyInput)
           (com.jme3.math ColorRGBA Vector3f)
           (com.jme3.material Material)
           (com.jme3.scene Geometry)
           (com.jme3.scene.shape Sphere)))

       

(defn three-sphere-in-front [this]
   (let [mat (Material. (.getAssetManager this)
                         "Common/MatDefs/Misc/Unshaded.j3md")
          cam (.getCamera this)
          rot (.getRotation cam)
          loc (.getLocation cam)
          root (.getRootNode this)]
      (dotimes [x 3]
        (.attachChild root
          (geom :shape (Sphere. 16 16 1)
                :label (str "Sphere " x) 
                :material mat
                :rotation rot
                :translation (let [res (Vector3f. loc)
                                   forward (Vector3f. (* x 3) 0 20)]
                               (.mult rot forward res)
                               res))))))   

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp []
      (input/on-key (.getInputManager this) KeyInput/KEY_F12
        [_ name value tpf]
        (three-sphere-in-front this)))))
 

(defn -main []
  (doto (make-app)
        (.start)))
