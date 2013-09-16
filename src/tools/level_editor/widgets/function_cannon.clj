(ns tools.level-editor.widgets.function-cannon
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.collision.shapes SphereCollisionShape]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Sphere])
  (:require [clojure.string :as str])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector transform]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(def globule-shape (Sphere. 32 32 (float 0.4)))
(def num-ball-color (ColorRGBA. 1 1 1 0.5))
(def num-font-color (ColorRGBA. 0 0 0 0.5))

(defprotocol Ballable
  (modify-ball! [val app font ball-node ball]))
(extend-protocol Ballable
  ColorRGBA
  (modify-ball! [color app ball-font ball-node ball]
    (.setMaterial ball 
       (material :asset-manager app
                 :color {"Color" color})))
  Number
  (modify-ball! [num app ball-font ball-node ball]
    (let [num-str (str num)]
      (.setMaterial ball
          (material :asset-manager app
                    :color {"Color" (ColorRGBA. 1 1 1 0.5)}))
      (.attachChild ball-node 
                                (bitmap-text 
                                      :size (/ 1.0 (count num-str) 2.0)
                                      :font ball-font
                                      :box (Rectangle. -0.3 0.3 0.3 0.3)
                                      :alignment :center
                                      :color num-font-color
                                      :text  num-str))
      (transparent! ball)
                  )))

(defn shoot-globule! [app on-error! font loc dir vel mass ball]
  (let [
        globule-phys* (RigidBodyControl. (SphereCollisionShape. 0.4) mass)
        globule (geom :shape globule-shape)
        globule* (node* :local-translation loc
                        :local-rotation dir
                        :children [globule]
                        :controls [globule-phys*])]
    (modify-ball! ball app font globule* globule)
    (doto globule*
      (attach-pog-fn!*
              (reify PogFn
                 (parameters [f] [])
                 (invoke [f env]
                   {:globule globule*
                    :on-error! on-error!
                    :value (constantly* ball)}))))
    (attach! app globule*)
    (.setLinearVelocity globule-phys*
                        (.mult (quaternion->direction-vector dir)
                               vel))))

(defn cannon-timer [app *queue* transform cannon-error! valid-input-type font spatial state nozzle-loc dir velocity mass balls env on-error!]
  (let [*balls* (atom balls)]
    (control-timer spatial 0.5 false
      (fn []
          (let [timer (computation-timer spatial
                        5
                        (fn []
                          (let [balls (value @*balls* env)]
                            (assert! (implements? clojure.lang.Seqable balls))
                            (when-not (empty? balls)
                            (let [ball (transform (first balls))
                                  more-balls (rest balls)]
                              (assert! (implements? valid-input-type ball))
                              [ball more-balls]))))
                        ;;Success
                        (fn [result]
                          (let-weave result
                            [ball (first result)
                             more-balls (second result)]
                            [queue @*queue*
                             more-balls queue]
                          [ball] :>> 
                             (do
                               (shoot-globule! app on-error! font nozzle-loc dir velocity mass ball)
                               (swap! *balls* (constantly more-balls)))
                          [queue] :>>
                            (do
                              (reset! *balls* queue)
                              (reset! *queue* [])
                              (if (empty? queue)
                                (reset! state {:state :inactive})))
                           (let [timer (cannon-timer app *queue* transform
                                                     cannon-error!
                                                     valid-input-type
                                                    font spatial state
                                                    nozzle-loc dir
                                                    velocity mass more-balls
                                                    env
                                                    on-error!)]
                            (swap! state (constantly {:timer timer
                                                      :state :active}))
                            (start! timer))))
                          ;;Failure
                          (fn [error]
                            (cannon-error!)
                            (on-error! error)
                            (swap! state (constantly {:state :inactive}))))]
            (swap! state (constantly {:state :active
                                      :timer timer}))
            (start! timer))))))


(defn build-function-cannon [{:keys [x z id direction velocity mass app
                                     queue?
                                     constraint
                                     transform-id]}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       ball-font (render-back! (bitmap-font :asset-manager app))
       control (RigidBodyControl. 0.0)
       *state* (atom {:state :inactive})
       *queue* (atom [])
       valid-input-type (cond (empty? constraint) (union-impl RGBA Number)
                           (= constraint "color") RGBA
                           (= constraint "number") Number)
       [cannon-error!
        transformer] (if (empty? transform-id) [(constantly nil) identity]
                       [(fn []
                          (on-bad-transform!
                            (spatial-pog-fn (select app transform-id))))
                        (fn [x] 
                         (transform
                           (spatial-pog-fn (select app transform-id)) x))])
       cannon (model :asset-manager app
                :model-name "Models/Laser/Laser.scene"
                :name id
                :local-translation loc
                :local-rotation dir
                     :controls [control])]
   (doto cannon
     (attach-pog-fn!* (reify
                          LazyPogFn
                          (lazy-invoke [_ env {player "player"
                                               on-error! "on-error!"
                                               balls "colors"}]
                            (let [state* @*state*]
                                (when (= (:state state*) :active)
                                  (if queue?
                                    (swap! *queue* concat balls)
                                    (stop! (:timer state*))))
                              (when-not (and (= (:state state*) :active)
                                             queue?)
                                (let [timer (cannon-timer app *queue*
                                                    transformer
                                                          cannon-error!
                                                          valid-input-type
                                                    ball-font cannon *state*
                                                           (.add loc 
                                                             (.mult dir 
                                                                    (Vector3f. 0 4 2.1)))
                                                          dir
                                                          (float velocity)
                                                          (float mass)
                                                          balls
                                                          env
                                                          on-error!)]
                                (swap! *state* (constantly {:state :active
                                                          :timer timer}))
                                 (start! timer)))))
                         PogFn
                         (parameters [_]
                          [{:name "player"
                            :type Warpable}
                           {:name "on-error!"
                            :type clojure.lang.IFn}
                           {:name "colors"
                           :type clojure.lang.Seqable}])
                          (docstring [_]
                          (docstr [["colors" "a list of colors"]]
                                  "Spits out a colored globule for each color in colors."))
 
                          )))))
 
(def function-cannon-template
  {:image (image-pad [100 100]
           (.drawRect 20 30 20 20)
           (.drawLine 20 30 40 50)
           (.drawLine 20 50 40 30)
           (.drawLine 40 50 50 40)
           (.drawLine 40 30 50 40))
   :questions [{:id :id        :type :string    :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :velocity  :type [:decimal
                                      :init 75] :label "Velocity"}
               {:id :mass      :type [:decimal
                                      :init 0.5] :label "Mass"}
               {:id :transform-id :type :string :label "Transformer"}
               {:id :queue?    :type :boolean    :label "Queue?"}
               {:id :contraint :type :string  :label "Constraint"}]
   :prelude `(use '~'tools.level-editor.widgets.function-cannon)
   :build     (fn [[x z] {:keys [id direction velocity mass
                                 queue? transform-id constraint]}]
                `(do
                   (fn [app#] 
                   (build-function-cannon {:x ~x :z ~z :id  ~id :direction ~direction :velocity ~velocity :mass  ~mass :constraint ~constraint :app app# :queue? ~queue? :transform-id ~transform-id}))))})

