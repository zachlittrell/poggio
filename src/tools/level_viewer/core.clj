(ns tools.level-viewer.core
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.input KeyInput]
           [com.jme3.material Material]
           [com.jme3.math Vector3f])
  (:require [jme-clj.input :as input])
  (:use [nifty-clj builders]
        [poggio level]
        [jme-clj material physics physics-control]
        [seesaw [core :only [input]]]))


(def player  (character-control :step-height (float 0.025)
                                :jump-speed 20
                                :fall-speed 30
                                :gravity 30
                                :physics-location (Vector3f. 0 0 0)))
(def walk-dir (Vector3f. 0 0 0))
(def key-ops
  {KeyInput/KEY_W [(atom false) :cam-dir]
   KeyInput/KEY_S [(atom false) :cam-dir :negate]
   KeyInput/KEY_A [(atom false) :cam-left]
   KeyInput/KEY_D [(atom false) :cam-left :negate]})

(defn set-up-keys! [input-manager]
  (input/on-key!* input-manager :action KeyInput/KEY_SPACE
      [_ name is-pressed? tpf]
        (when is-pressed?
          (.jump player)))
  (doseq [[key [atom  _]] key-ops]
    (input/on-key!* input-manager :action key
      [_ name is-pressed? tpf]
      (swap! atom (constantly is-pressed?)))))


(defn set-up-room! [app level-fn]
  (.add (physics-space app) player)
  (load-level (level-fn (.getAssetManager app))
                           ;;(textured-material (.getAssetManager app)
                           ;;                   "Textures/Terrain/BrickWall/BrickWall.jpg"))
              app
              player)
  (.setLocation (.getCamera app) (.getPhysicsLocation player)) )

(defn make-app [level-fn]
  (proxy [SimpleApplication][]
    (simpleUpdate[tpf]
      (let [cam (.getCamera this)
            cam-dir (-> cam
                        (.getDirection)
                        (.clone)
                        (.multLocal (float 0.4))
                        (.setY 0)
                        (.normalize)
                        (.multLocal (float 0.3)))
            cam-left (-> cam
                         (.getLeft)
                         (.clone)
                         (.multLocal (float 0.3)))]
        (.set walk-dir 0 0 0)
        (doseq [[_ [is-pressed? addend multiplicand]] key-ops]
          (when @is-pressed? 
            (let [addend (if (= addend :cam-left) cam-left cam-dir)]
              (.addLocal walk-dir (if (= multiplicand :negate)
                                    (.negate addend)
                                    addend)))))
        (.setWalkDirection player walk-dir)
        (.setLocation cam (.getPhysicsLocation player))))
    (simpleInitApp []
      (doto (.getStateManager this)
        (.attach (BulletAppState.)))
      (doto (.getInputManager this)
        (set-up-keys!))
      (doto this
        (set-up-room! level-fn)))))

(defn -main [& args]
  (let [level (if (empty? args)
                (eval (read-string (input "Enter level function")))
                (eval (read-string (slurp (first args)))))]
    (doto (make-app level)
      (.start))))
