(ns poggio.test.poggio.level-test
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.input KeyInput]
           [com.jme3.material Material]
           [com.jme3.math Vector3f])
  (:require [jme-clj.input :as input])
  (:use [nifty-clj builders]
        [poggio level]
        [jme-clj material physics physics-control]))


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


(defn set-up-room! [app]
  (.add (physics-space app) player)
  (load-level (basic-level ;;[1 1] #{[3 2] [1 0] [3 3] [0 0] [2 3] [0 1] [0 2] [1 3] [0 3] [3 0] [3 1] [2 0]} 
                         ;; [2 1] #{[4 3] [1 0] [3 3] [0 0] [2 3] [0 1] [0 2] [1 3] [0 3] [6 0] [5 0] [6 1] [4 0] [6 2] [3 0] [6 3] [5 3] [2 0]} 
 [4 3] #{[3 2] [5 4] [8 7] [1 0] [3 3] [4 4] [8 8] [0 0] [3 4] [7 8] [0 1] [4 6] [7 9] [0 2] [1 3] [4 7] [6 9] [0 3] [1 4] [5 9] [1 5] [4 9] [1 6] [3 9] [1 7] [2 9] [1 8] [1 9] [6 0] [5 0] [6 1] [9 4] [4 0] [6 2] [8 4] [9 5] [3 0] [6 3] [7 4] [9 6] [6 4] [9 7] [2 0]}


                           (textured-material (.getAssetManager app)
                                              "Textures/Terrain/BrickWall/BrickWall.jpg"))
              app
              player)
  (.setLocation (.getCamera app) (.getPhysicsLocation player)) )

(defn make-app []
  (proxy [SimpleApplication][]
    (simpleUpdate[tpf]
      (let [cam (.getCamera this)
            cam-dir (-> cam
                        (.getDirection)
                        (.clone)
                        (.multLocal (float 0.4))
                        (.setY 0)
                        (.normalize)
                        (.multLocal (float 0.1)))
            cam-left (-> cam
                         (.getLeft)
                         (.clone)
                         (.multLocal (float 0.1)))]
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
        (set-up-room!)))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
