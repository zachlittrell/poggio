(ns poggio.test.poggio.level-test
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.material Material])
  (:use [nifty-clj builders]
        [poggio level]
        [jme-clj material physics]))

(defn wall-mat [app]
  (let [mat (Material. (.getAssetManager app)
                       "Common/MatDefs/Misc/Unshaded.j3md")]
   ;; (.setWireframe (.getAdditionalRenderState mat) true)
    mat))

(defn set-up-room! [app]
  (load-level (basic-level [1 1] #{[3 2] [1 0] [3 3] [0 0] [2 3] [0 1] [0 2] [1 3] [0 3] [3 0] [3 1] [2 0]} 
                           (textured-material (.getAssetManager app)
                                              "Textures/Terrain/BrickWall/BrickWall.jpg"))
              app
              nil))

(defn make-app []
  (proxy [SimpleApplication][]
    (simpleInitApp []
      (doto (.getStateManager this)
        (.attach (BulletAppState.)))
      (doto this
        (set-up-room!)))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
