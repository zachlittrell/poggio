(ns poggio.test.poggio.physics-test
  "An adaptation of JMonkeyEngine's Hello Physics tutorial
   to test poggio's physics capabilities"
  (:use [jme-clj bitmap-text geometry input material physics])
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.asset TextureKey]
           [com.jme3.bullet BulletAppState]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.input MouseInput]
           [com.jme3.math Vector2f Vector3f]
           [com.jme3.scene.shape Box Sphere Sphere$TextureMode]
           [com.jme3.texture Texture$WrapMode]))

(def wall-mat (atom nil))
(def stone-mat (atom nil))
(def floor-mat (atom nil))

(defn set-up-materials! [app]
  (let [assets (.getAssetManager app)]
    (swap! wall-mat
           (constantly 
             (material :asset-manager assets
                       :def-name "Common/MatDefs/Misc/Unshaded.j3md"
                       :texture {"ColorMap" 
                                 (texture 
                                   :asset-manager assets
                                   :texture-key 
                                     (texture-key 
                                      :name "Textures/Terrain/BrickWall/BrickWall.jpg"
                                      :generate-mips true))})))
     (swap! stone-mat
           (constantly 
             (material :asset-manager assets
                       :def-name "Common/MatDefs/Misc/Unshaded.j3md"
                       :texture {"ColorMap" 
                                 (texture
                                   :asset-manager assets
                                   :texture-key
                                     (texture-key 
                                      :name "Textures/Terrain/Rock/Rock.PNG"
                                      :generate-mips true))})))
      (swap! floor-mat
           (constantly 
             (material :asset-manager assets
                       :def-name "Common/MatDefs/Misc/Unshaded.j3md"
                       :texture {"ColorMap" 
                                 (texture
                                  :asset-manager assets
                                  :texture-key 
                                   (texture-key 
                                    :name "Textures/Terrain/Pond/Pond.jpg"
                                    :generate-mips true)
                                  :wrap :repeat)})))))
            
(def floor-shape (doto (Box. Vector3f/ZERO (float 10) (float 0.1) (float 5))
                   (.scaleTextureCoordinates (Vector2f. 3 6))))

(defn set-up-floor! [app]
  (attach! app
           (geom :label "Floor"
                 :shape floor-shape
                 :material @floor-mat
                 :local-translation (Vector3f. 0 (float 0.1) 0)
                 :controls [(RigidBodyControl. (float 0))])))

(def brick-length (float 0.48))
(def brick-width (float 0.24))
(def brick-height (float 0.12))

(def box (doto (Box. Vector3f/ZERO brick-length brick-height brick-width)
           (.scaleTextureCoordinates (Vector2f. (float 1) (float 0.5)))))

(defn add-brick! [app loc]
  (attach! app
           (geom :label "Brick"
                 :shape box
                 :material @wall-mat
                 :local-translation loc
                 :controls [(RigidBodyControl. (float 2))])))

(defn set-up-wall! [app]
  (loop [start (/ brick-length 4)
         height 0
         j      0]
    (when (< j 15)
      (dotimes [i 6]
        (add-brick! app (Vector3f. (+ (* i brick-length 2)
                                      start)
                                   (+ brick-height height)
                                   0)))
      (recur (- start)
             (+ height (* 2 brick-height))
             (inc j)))))

(defn set-up-cross-hairs! [app]
  (let [font (-> (.getAssetManager app)
                 (.loadFont "Interface/Fonts/Default.fnt"))
        settings (-> (.getContext app)
                     (.getSettings))
        size (-> font
                 (.getCharSet)
                 (.getRenderedSize)
                 (* 2))
        text (bitmap-text :font font
                          :size size
                          :text "+")]
    (doto (.getGuiNode app)
      (.detachAllChildren)
      (.attachChild 
        (doto text
          (.setLocalTranslation (- (/ (.getWidth settings) 2)
                                   (/ size 3))
                                (+ (/ (.getHeight settings) 2)
                                   (/ (.getLineHeight text) 2))
                                0))))))

(def sphere (doto (Sphere. 32 32 (float 0.4) true false)
              (.setTextureMode Sphere$TextureMode/Projected)))

(defn make-cannon-ball! [app]
  (let [ball-phys (RigidBodyControl. (float 1))]
    (attach! app
             (geom :label "CannonBall"
                   :shape sphere
                   :material @stone-mat
                   :local-translation (.getLocation (.getCamera app))
                   :controls [ball-phys]))
    (.setLinearVelocity ball-phys (.mult (.getDirection (.getCamera app))
                                         (float 25)))))


(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp []
      (doto (.getStateManager this)
        (.attach (BulletAppState.)))
      (doto (.getCamera this)
        (.setLocation (Vector3f. 0 (float 4) (float 6)))
        (.lookAt (Vector3f. 2 2 0) Vector3f/UNIT_Y))
      (doto (.getInputManager this)
        (on-mouse-button!* :action MouseInput/BUTTON_LEFT
          [_ name is-pressed? tpf]
            (when-not is-pressed?
              (make-cannon-ball! this))))
      (doto this
        (set-up-materials!)
        (set-up-wall!)
        (set-up-floor!)
        (set-up-cross-hairs!)))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
