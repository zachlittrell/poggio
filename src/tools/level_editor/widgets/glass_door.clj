(ns tools.level-editor.widgets.glass-door
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box])
  (:use [control bindings]
        [data coll color ring-buffer quaternion]
        [jme-clj animate control geometry material model node physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color]
        [seesawx core]))
;
(defn door-alarm [time speed state door-node initial-location]
  (alarm-control time
      (fn []
        (let [state* @state]
            (swap! state 
                   (constantly {:state :closing
                                :closer (follow-path! door-node speed
                                          (doto (motion-path [(.getLocalTranslation door-node)
                                           initial-location])
                                            (.addListener
                                              (motion-path-listener
                                                (fn [motion-control index]
                                                  (swap! state
                                                    (constantly
                                                    {:state :closed
                                                     :closer motion-control}))
                                                  )))))}))))))


(defn toggle-door! [door-node state initial-location open? movement speed time app]
  (let [state* @state]
    ;;We want to toggle the door only if it is not already opening
    (if (= (:state state*) :opened)
      (do
        (.removeControl door-node (:close-timer state*))
        (let [alarm (door-alarm time speed state door-node initial-location)]
          (swap! state (constantly {:state :opened :close-timer alarm}))
          (.addControl door-node alarm)))
    (when (not= (:state state*) :opening)
      (if (= (:state state*) :closing)
        ;;If the door is closing, stop the closing process
        (.stop (:closer state*))
        (if (= (:state state*) :opened)
          ;;If the door is opened, stop the previous timer.
          (.removeControl door-node (:close-timer state*))))
      (let [path (motion-path [(.getLocalTranslation door-node)
                               (.add initial-location
                                     (.mult (.getLocalRotation door-node)
                                            movement))])]
        (when (> time 0)
          (.addListener path 
                        (motion-path-listener
                          (fn [motion-control index]
                            (when (== index 1)
                              (let [alarm (door-alarm time speed
                                                      state door-node 
                                                      initial-location)]
                                (swap! state (constantly {:state :opened
                                                          :opener motion-control
                                                          :close-timer alarm}))
                              (.addControl door-node 
                                alarm)))))))
        (swap! state (constantly {:state :opening
                                  :opener (follow-path! door-node speed path)})))))))



(def movement->vector
  {:up (Vector3f. 0 16 0)
   :down (Vector3f. 0 -16 0)
   :left (Vector3f. -16 0 0)
   :right (Vector3f. 16 0 0)
   :forward (Vector3f. 0 0 16)
   :backward (Vector3f. 0 0 -16)})

(defn build-glass-door [x z id direction distance movement speed time app]
  (let [loc (Vector3f. (* x 16) -8  (* z 16))
        dir (angle->quaternion direction :y)
        control (RigidBodyControl. 1.0)
        state (atom {:state :closed})
        door (geom :shape (Box. 8 8 1)
                   :controls [control]
                   :material (material :asset-manager app
                                       :texture {"ColorMap"
                                                 (texture :asset-manager app
                                                          :texture-key "Textures/water1.png")}))]
;                                       :color {"Color" 
;                                               (ColorRGBA. 0.85 1 0.94 0.5)}))]
    [(pog-fn-node :children [(transparent! door)]
                 :name id
                 :pog-fn (fn->pog-fn (fn [open?]
                                       (if (instance? Boolean open?)
                                       (toggle-door! (.getParent door) 
                                                     state
                                                     loc
                                                     open? 
                                                     (movement->vector
                                                       movement)
                                                     speed
                                                     time app)))
                                     ""
                                     ["open?"])
                 :local-translation loc
                 :local-rotation dir
                 :controls [control])
     :kinematic? true]))

(def glass-door-template
  {:image (image-pad [100 100]
            (.drawString "◫" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :distance :type :decimal :label "Distance"}
               {:id :movement :type [:choice
                                     :model [:up :down :left :right :forward
                                             :backward]]
                              :label "Movement"}
               {:id :speed :type :integer :label "Speed"}
               {:id :time :type :integer :label "Time"}]
   :prelude `(use '~'tools.level-editor.widgets.glass-door)
   :build (fn [[x z] {:keys [id direction distance movement speed time]}]
           `(do
                 (fn [app#] 
                 (build-glass-door ~x ~z ~id ~direction ~distance ~movement ~speed ~time app#))))})

;;(defn build-glass-door-sleeve [x z direction app]
;;  (let [loc (Vector3f. (* x 16) -8 (* z 16))
;;        dir (angle->quaternion direction :y)
;;        control (RigidBodyControl. 0.0)]
;;    (node :local-translation loc
;;          :local-rotation dir
;;          :controls[control]
;;          :children [(geom :shape (Box. 8 8 1)
;;                           :controls [(RigidBodyControl. 0)]
;;                           )])))
;;
;;(def glass-door-sleeve-template
;;  {:image (image-pad [100 100]
;;            (.drawString "≎" 49 49))
;;   :questions [{:id :direction :type :direction :label "Direction"}]
;;   :prelude `(use '~'tools.level-editor.widgets.glass-door)
;;   :build (fn [[x z] {:keys [direction]}]
;;            `(fn [app#]
;;               (build-glass-door ~x ~z ~direction app#)))})
