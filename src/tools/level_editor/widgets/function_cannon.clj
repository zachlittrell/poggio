(ns tools.level-editor.widgets.function-cannon
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Sphere])
  (:use [data coll color ring-buffer quaternion]
        [jme-clj animate control geometry material model physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph color]
        [seesawx core]))

(def globule-shape (Sphere. 32 32 (float 0.4)))
(defn shoot-globule! [app loc dir vel mass color]
  (let [globule-phys (RigidBodyControl. mass)
        globule-phys* (RigidBodyControl. mass)
        globule (geom :shape globule-shape
                      ;;:local-translation loc
                      ;;:local-rotation dir
                      :material (material :asset-manager app
                                          :color {"Color" color})
                      :controls [globule-phys*])]
    (attach! app
             (pog-fn-node 
                :local-translation loc
                :pog-fn (constantly* (color->triple color))
                :local-rotation dir
                :children [globule]
                :controls [globule-phys*]))
    (.setLinearVelocity globule-phys*
                        (.mult (quaternion->direction-vector dir)
                               vel))))

(defn cannon-timer [app state nozzle-loc dir velocity mass balls]
  (let [*balls* (atom balls)]
    (timer-control 0.5 true
      (fn []
        (if-let [[ball & more-balls] (seq @*balls*)]
          (do
            (if (instance? ColorRGBA ball)
              (shoot-globule! app nozzle-loc dir velocity mass ball))
            (swap! *balls* (constantly more-balls)))
          (do
           (swap! state (constantly {:state :inactive}))
            false)
            )))))


(defn build-function-cannon [x z id direction velocity mass app]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       *state* (atom {:state :inactive})
       cannon (model :asset-manager app
                :controls [control]
                    :model-name "Models/Laser/Laser.scene")]
   (pog-fn-node :name id
                :local-translation loc
                :local-rotation dir
                :controls [control]
                :pog-fn (fn->pog-fn 
                          (fn [player balls]
                            (let [state* @*state*
                                  balls (value balls)]
                              (when (coll? balls)
                                (when (= (:state state*) :active)
                                  (.removeControl cannon (:timer state*)))
                                (let [timer (cannon-timer app *state*
                                                           (.add loc 
                                                             (.mult dir 
                                                                    (Vector3f. 0 4 2.1)))
                                                          dir
                                                          (float velocity)
                                                          (float mass)
                                                          balls)]
                                (.addControl cannon timer)
                                (swap! *state* (constantly {:state :active
                                                          :timer timer}))))))
                                      ""
                                     [{:name "player"
                                       :type Warpable}
                                      {:name "colors"
                                       :type clojure.lang.Seqable}])
                :children [cannon])))
 
(def function-cannon-template
  {:image (image-pad [100 100]
           (.drawRect 20 30 20 20)
           (.drawLine 20 30 40 50)
           (.drawLine 20 50 40 30)
           (.drawLine 40 50 50 40)
           (.drawLine 40 30 50 40)
                     )
   :questions [{:id :id        :type :string    :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :velocity  :type :decimal   :label "Velocity"}
               {:id :mass      :type :decimal   :label "Mass"}]
   :prelude `(use '~'tools.level-editor.widgets.function-cannon)
   :build     (fn [[x z] {:keys [id direction velocity mass]}]
                `(do
                   (fn [app#] 
                   (build-function-cannon ~x ~z ~id ~direction ~velocity ~mass app#))))})

