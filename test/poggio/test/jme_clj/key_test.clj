(ns poggio.test.jme-clj.key-test
  (:use [jme-clj [geometry :only [geom]]])
  (:require [jme-clj.input :as input])
  (:import (com.jme3.app SimpleApplication)
           (com.jme3.input KeyInput)
           (com.jme3.math ColorRGBA Vector3f Quaternion)
           (com.jme3.material Material)
           (com.jme3.scene Geometry)
           (com.jme3.scene.shape Sphere)
           (com.jme3.niftygui.NiftyJmeDisplay)))

       

(defn three-sphere-in-front [^SimpleApplication this]
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
                :move (let [res (Vector3f.)
                            forward (Vector3f. (* x 3) 0 20)]
                        (.mult rot forward res)
                        (.add loc res))))))) 

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp []
      (doto (.getInputManager this)
        (input/on-key!* :action KeyInput/KEY_F12
          [_ name is-pressed? tpf]
          (if is-pressed?
            (three-sphere-in-front this)))))))
 

(defn -main [& args]
  (doto (make-app)
        (.start)))

