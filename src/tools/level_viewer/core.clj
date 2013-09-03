(ns tools.level-viewer.core
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.input KeyInput MouseInput]
           [com.jme3.material Material]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.system AppSettings])
  (:require [jme-clj.input :as input])
  (:use [data coll]
        [nifty-clj builders]
        [poggio level loading-gui quotes]
        [poggio.functions core color modules list gui scenegraph]
        [jme-clj collision light material physics physics-providers 
         physics-control]
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

(defn set-up-collisions! [app]
  (on-pred-collision! app [#(and % (is-pog-fn-spatial? % 1) %)
                           #(and % (is-pog-fn-spatial? % 0) %)]
      [this f input e]
      (when (and (.getParent f) (.getParent input))
        (try
        (invoke* (spatial-pog-fn f) {} [(spatial-pog-fn input)])
        (catch Exception e
          (.printStackTrace e))))))

(defn loading-screen! [nifty]
  (let [{:keys [author quote]} (random quotes)]
    (set-headers! (.getScreen nifty "loading-screen")
                  :header author :subheader quote)
    (.gotoScreen nifty "loading-screen")))

(defn set-up-keys! [nifty set-current-function! input-manager camera clickable]
  (input/on-key!* input-manager :action KeyInput/KEY_SPACE
      [_ name is-pressed? tpf]
        (when is-pressed?
          (.jump player)))
  (doseq [[key [atom  _]] key-ops]
    (input/on-key!* input-manager :action key
      [_ name is-pressed? tpf]
      (swap! atom (constantly is-pressed?))))
  (input/on-mouse-button!* input-manager :action MouseInput/BUTTON_LEFT
      [_ name is-pressed? tpf]
      (when is-pressed?
        (when-let [collision (closest-collision-from-point camera 
                                                           (.getCursorPosition
                                                             input-manager)
                                                           clickable)]
          (when-let [pog-fn (spatial-pog-fn (.getGeometry collision))]
            (when (and (== 3 (count (parameters pog-fn)))
                       (pog-implements? (first (parameters pog-fn)) player))
              (set-current-function! (partial* pog-fn
                                               {(name* (first (parameters
                                                                pog-fn)))
                                                player})))
        )))))


(defn set-up-room! [app level nifty]
  (pause-physics! app)
  (let [root (.getRootNode app)]
    (when-not (zero? (.getQuantity root))
      (.detachChildAt root 0)))
  (loading-screen! nifty)
  (future
    (let [space (physics-space app)]
      (let [level (load-level (basic-level level)
                              app
                              [(.getCamera app) player]
                              [(.getCamera app)])]
        (doto level
          (.addLight (ambient-light :color (.mult ColorRGBA/White 1.3))))
       (.add space player)
      (.enqueue app
        (fn []
           (.attachChild (.getRootNode app) level)
           (start-physics! app)
           (.gotoScreen nifty  "function-screen")))
    ))))

(defn make-app [level]
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
     (doto (.getGuiNode this)
          (.detachAllChildren))
    (let [[display nifty] (nifty this true)
          {:keys [set-current-function!
                  function-screen]} (function-screen nifty
                                        (assoc core-modules
                                          "TEST" 
                                          {"BAD" (fn->pog-fn 
                                                   (fn [foo]
                                                     (Thread/sleep 6000)
                                                     [red*])
                                                   "bad"
                                                   ["foo"]
                                                   (apply str (repeat 100 "str")))}))]
        (.addScreen nifty "function-screen" function-screen)
      ;;  (.gotoScreen nifty "function-screen")
        (.addScreen nifty "loading-screen" (build-screen nifty (loading-gui)))
 
            (doto (.getStateManager this)
             (.attach (BulletAppState.)))
        (set-up-keys! nifty set-current-function!
                      (.getInputManager this)
                      (.getCamera this) (.getRootNode this))
            (doto (.getFlyByCamera this)
        (.setDragToRotate true))
       ; (loading-screen! nifty)
      (doto this
      
        (set-up-room! level nifty)
        (set-up-collisions!))

                     
                   
                   ))))

(defn view-level [level]
  (cond (string? level) (recur (eval (read-string level)))
        (map? level) (send (agent (make-app level)) #(.start %))))

(defn -main [& args]
  (let [level (if (empty? args)
                (eval (read-string (input "Enter level map")))
                (load-string (slurp (first args))))]
    (let [app (make-app level)]
      (.setSettings app (doto (AppSettings. true)
                          (.setSettingsDialogImage "Textures/splashscreen.png")))
      (.start app))))
