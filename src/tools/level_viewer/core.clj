(ns tools.level-viewer.core
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.bullet BulletAppState]
           [com.jme3.input KeyInput MouseInput]
           [com.jme3.material Material]
           [com.jme3.math ColorRGBA Vector3f])
  (:require [jme-clj.input :as input])
  (:use [data coll]
        [nifty-clj builders]
        [poggio level]
        [poggio.functions core color list gui scenegraph]
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
  (on-pred-collision! app [#(pog-fn-node-from % 1)
                           #(pog-fn-node-from % 0)]
      [this f input e]
      (when (and (.getParent f) (.getParent input))
        (invoke f [input]))))


(defn set-up-keys! [nifty function-screen *fn-map* input-manager camera clickable]
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
          (when-let [pog-fn (pog-fn-node-from (.getGeometry collision))]
            (when (and (<= 2 (count (parameters pog-fn)))
                       (pog-implements? (first (parameters pog-fn)) player))
              (set-current-function! nifty 
                                     function-screen 
                                     (partial* pog-fn 
                                               {(name* (first (parameters
                                                                pog-fn))) player})
                                     *fn-map*))
        )))))


(defn set-up-room! [app level]
  (doto (.getRootNode app)
    (.addLight (ambient-light :color (.mult ColorRGBA/White 1.3))))
  (.add (physics-space app) player)
  (load-level (basic-level level)
              app
              [(.getCamera app) player]
              [(.getCamera app)]))

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
          [*fn-map* function-screen] (function-screen nifty
                                       ["red" red*]
                                       ["green" green*]
                                       ["blue" blue*]
                                       ["()" empty-list*]
                                       ["cons" cons*]
                                       ["triple" triple*]
                                       ["repeat" repeat*]
                                       ["concat" concat*]
                                       ["flatten" flatten*])]
        (.addScreen nifty "function-screen" function-screen)
        (.gotoScreen nifty "function-screen")

 
            (doto (.getStateManager this)
        (.attach (BulletAppState.)))
        (set-up-keys! nifty function-screen *fn-map* 
                      (.getInputManager this)
                      (.getCamera this) (.getRootNode this))
            (doto (.getFlyByCamera this)
        (.setDragToRotate true))
      (doto this
        (set-up-room! level)
        (set-up-collisions!))

                     
                   
                   ))))

(defn view-level [level]
  (cond (string? level) (recur (eval (read-string level)))
        (map? level) (send (agent (make-app level)) #(.start %))))

(defn -main [& args]
  (let [level (if (empty? args)
                (eval (read-string (input "Enter level map")))
                (load-string (slurp (first args))))]
    (doto (make-app level)
      (.start))))
