(ns poggio.test.jme-clj.collision-test
  "An adaptation of JMonkeyEngine's Hello Collision tutorial
   using poggio"
  (:use [jme-clj geometry light physics physics-control])
  (:require [jme-clj.input :as input])
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.asset.plugins ZipLocator]
           [com.jme3.bullet BulletAppState]
           [com.jme3.bullet.collision.shapes CapsuleCollisionShape]
           [com.jme3.bullet.control CharacterControl RigidBodyControl]
           [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.input KeyInput]
           [com.jme3.math ColorRGBA Vector2f Vector3f]
           [com.jme3.scene Node]
           [com.jme3.scene.shape Box Sphere]))

(def left (atom false))
(def right (atom false))
(def up (atom false))
(def down (atom false))

(def walk-dir (Vector3f.))
(def player (character-control :shape         (CapsuleCollisionShape. 1.5 6 1)
                               :step-height      (float 0.05)
                               :jump-speed       20
                               :fall-speed       30
                               :gravity          30
                               :physics-location (Vector3f. 0 10 0)))

(defn set-up-keys! [app]
  (doto (.getInputManager app)
    (input/on-key!* :action KeyInput/KEY_SPACE
      [_ name is-pressed? tpf]
        (if is-pressed?
          (.jump player))))
  (doseq [[atom key] {left  KeyInput/KEY_A
                      right KeyInput/KEY_D
                      up    KeyInput/KEY_W
                      down  KeyInput/KEY_S}]
    (doto (.getInputManager app)
      (input/on-key!* :action key
        [_ name is-pressed? tpf]
        (swap! atom (constantly is-pressed?))))))

(defn set-up-light! [app]
  (doto (.getRootNode app)
    (.addLight (ambient-light :color (.mult ColorRGBA/White 1.3)))
    (.addLight (directional-light :color ColorRGBA/White
                                  :direction (-> (Vector3f. 2.8 -2.8 -2.8)
                                                 (.normalizeLocal))))))

(defn set-up-scene! [app]
  (attach! app
           (let [asset-manager (doto (.getAssetManager app)
                                     (.registerLocator "town.zip"
                                                       ZipLocator))
                 scene ^Node (.loadModel asset-manager "main.scene")]
             (.setLocalScale scene (float 2.0))
             (.addControl scene (RigidBodyControl.
                                  (CollisionShapeFactory/createMeshShape
                                    scene)
                                  0))
             scene))
  (.add (physics-space app) player))

(defn make-app []
  (proxy [SimpleApplication][]
    (simpleUpdate [tpf]
      (let [cam (.getCamera this)
            cam-dir (-> cam 
                        (.getDirection)
                        (.clone)
                        (.multLocal (float 0.4))
                        (.setY 0)
                        (.normalize))
            cam-left (-> cam
                         (.getLeft)
                         (.clone)
                         (.multLocal (float 0.4)))]
        (.set walk-dir 0 0 0)
        (when @left (.addLocal walk-dir cam-left))
        (when @right (.addLocal walk-dir (.negate cam-left)))
        (when @up (.addLocal walk-dir cam-dir))
        (when @down (.addLocal walk-dir (.negate cam-dir)))
        (.setWalkDirection player walk-dir)
        (.setLocation cam (.getPhysicsLocation player))))
    (simpleInitApp []
      (.attach (.getStateManager this) (BulletAppState.))
      (.setBackgroundColor (.getViewPort this) (ColorRGBA. 0.7 0.8 1 1))
      (.setMoveSpeed (.getFlyByCamera this) 100)
      (set-up-keys! this)
      (set-up-light! this)
      (set-up-scene! this))))

(defn -main [& args]
  (doto (make-app)
        (.start)))
