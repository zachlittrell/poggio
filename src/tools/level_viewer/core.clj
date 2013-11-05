(ns tools.level-viewer.core
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.input KeyInput MouseInput]
           [com.jme3.material Material]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Spatial Geometry]
           [com.jme3.system AppSettings])
           ;[tools.level_viewer.context LevelContext])
  (:require [jme-clj.audio :as audio]
            [jme-clj.input :as input]
            [tools.level-editor.templates :as templates]
            [tools.level-viewer.context :as context])
  (:use [control io]
        [data coll object]
        [jme-clj audio spatial]
        [nifty-clj builders]
        [poggio level loading-gui quotes]
        [poggio.functions core color modules list gui scenegraph]
        [jme-clj collision light material node physics physics-providers 
         physics-control]
        [seesaw [core :only [input]]]))

(def init-gui-key "init-gui")

(def level-end-key "is-level-end?")

(defn is-level-end? [spatial]
  (get-user-data! spatial level-end-key))

(defn level-end! [spatial]
  (set-user-data! spatial level-end-key :level-end)
  spatial)

(def end-level-key "end-level!")

(defn end-level! [app success?]
  (if-let [end-level! (get-user-data! (.getRootNode app) end-level-key)]
    (end-level! success?)
    (.stop app)))

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
  (let [n (node*)
        dummy (node* :children [n])]
    (.setUserObject player n)
    (attach-pog-fn! n
      (constantly* player))
    (on-pred-collision! app [#(and % (is-pog-fn-spatial? % 1) %)
                             #(and % (is-pog-fn-spatial? % 0) %)]
        [this f input e]
        (when (and (.getParent f) (.getParent input))
          (try
          (invoke* (spatial-pog-fn f) {} [(spatial-pog-fn input)])
          (catch Exception e
            ))))))

(defn loading-screen! [nifty]
  (let [{:keys [author quote]} (random quotes)]
    (set-headers! (.getScreen nifty "loading-screen")
                  :header author :subheader quote)
    (.gotoScreen nifty "loading-screen")))

(defn set-up-keys! [nifty alert! set-current-function! input-manager app camera clickable]
  (.deleteMapping input-manager SimpleApplication/INPUT_MAPPING_EXIT)
  (input/on-key! input-manager :action SimpleApplication/INPUT_MAPPING_EXIT
                               KeyInput/KEY_ESCAPE
      [_ name is-pressed? tpf]
        (when is-pressed?
          (context/end-level! app false)))
  (input/on-key!* input-manager :action KeyInput/KEY_SPACE
      [_ name is-pressed? tpf]
        (when is-pressed?
          (.jump player)))
  (doseq [[key [atom  _]] key-ops]
    (input/on-key!* input-manager :action key
      [_ name is-pressed? tpf]
      (reset! atom is-pressed?)))
  (input/on-mouse-button!* input-manager :action MouseInput/BUTTON_RIGHT
      [_ name is-pressed? tpf]
      (when is-pressed?
        (when-let [collision (closest-collision-from-point camera
                                                      (.getCursorPosition
                                                        input-manager)
                                                      clickable)]
          (when-let [pog-fn (spatial-pog-fn (.getGeometry collision))]
            (let [docstr (docstring pog-fn)]
              (when-not (empty docstr)
                (alert! (Exception. docstr))))))))

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
                                                player})
                                     (pog-fn-name (.getGeometry collision))))
        )))))

(defn set-up-room! [app level nifty on-error!]
  (pause-physics! app)
  (let [root (.getRootNode app)
        child (when-not (zero? (.getQuantity root))
                (.detachChildAt root (dec (.getQuantity root))))]
  (loading-screen! nifty)
  (future
    (try
      (let [space (physics-space app)]
        (if child
          (.removeAll space child))
        (let [level* (templates/eval-level
                       (if (fn? level) (level) level))
              level (load-level (basic-level level*)
                                app
                                on-error!
                                [(.getCamera app) player]
                                [(.getCamera app)])]
          (doto level
            (.addLight (ambient-light :color (.mult ColorRGBA/White 1.3))))
         (.add space player)
         (when-let [initialize! (get-user-data! root init-gui-key)]
           (initialize!)
           (remove-user-data! root init-gui-key))
          ;;This is to make sure the loading screen is done fading in
         (Thread/sleep 1000)
        (.enqueue app
          (fn []
             (.attachChild (.getRootNode app) level)
             (start-physics! app)
             (audio/play! app (:soundtrack level*))
             (.gotoScreen nifty  "function-screen")))))
          (catch Exception e
            (.printStackTrace e)
            (System/exit -1))
    ))))

(defn make-app 
  ([level]
   (make-app 
     (fn [app nifty on-error!] (set-up-room! app level nifty on-error!))
     (fn [app nifty success?] (.stop app))))
  ([setup! end-level! & {:as opts-map}]
    (doto
  (let [*end-level-watchers* (atom [])]
  (proxy [SimpleApplication 
          tools.level_viewer.context.LevelContext][]
    (add_end_level_watch_BANG_ [watch]
        (swap! *end-level-watchers* conj watch))
    (end_level_BANG_ [success?] 
        (doseq [watcher @*end-level-watchers*]
          (watcher))
        (reset! *end-level-watchers* [])
        (tools.level-viewer.core/end-level! this success?))
    (stop []
      (proxy-super stop)
      (if (:shutdown? opts-map)
        (shutdown-agents)))
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
                  function-screen
                  alert!
                  initialize!]} (function-screen nifty core-modules
                                        ;;(assoc core-modules
                                        ;;  "TEST" 
                                        ;;  {"BAD" (fn->pog-fn 
                                        ;;           (fn [foo]
                                        ;;             (Thread/sleep 6000)
                                        ;;             [red*])
                                        ;;           "bad"
                                        ;;           ["foo"]
                                        ;;           (apply str (repeat 100 "str")))})
                                            )]
        (.addScreen nifty "function-screen" function-screen)
        (.addScreen nifty "loading-screen" (build-screen nifty (loading-gui)))
 
            (doto (.getStateManager this)
             (.attach (BulletAppState.))
             (.attach (soundtrack-dj this)))
        (set-up-keys! nifty alert! set-current-function!
                      (.getInputManager this)
                      this
                      (.getCamera this) (.getRootNode this))
            (doto (.getFlyByCamera this)
        (.setDragToRotate true))
      (doto this
        ;(set-up-room! level nifty)
        (set-up-collisions!))
      (setup! this nifty alert!)
      (set-user-data! (.getRootNode this) init-gui-key initialize!)
      (set-user-data! (.getRootNode this) end-level-key (partial end-level! this nifty))

                   ))))
      (.setSettings  (doto (AppSettings. true)
             (.setSettingsDialogImage "Textures/splashscreen.png"))))))

(defn view-level [level]
  (cond (string? level) (future (doto (make-app (eval (read-string level)))
                                      (.start)))
        (map? level) 
          (future (doto (make-app level)
                        (.start)))))
        
(defn -main [& args]
  (view-level (slurp (first args))))
