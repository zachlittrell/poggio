(ns tools.level-editor.widgets.function-cannon
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.collision.shapes SphereCollisionShape]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box Sphere])
  (:require [clojure.string :as str])
  (:use [control assert timer]
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
  BigDecimal
  (modify-ball! [num app ball-font ball-node ball]
    (.setMaterial ball
        (material :asset-manager app
                  :color {"Color" (ColorRGBA. 1 1 1 0.5)}))
    (.attachChild ball-node 
                              (bitmap-text 
                                    :size 0.5
                                    :font ball-font
                                    :box (Rectangle. -0.3 0.3 0.3 0.3)
                                    :color num-font-color
                                    :text  (str num)))
    (transparent! ball)
                ))

(defn shoot-globule! [app font loc dir vel mass ball]
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
                    :value (constantly* ball)}))))
    (attach! app globule*)
    (.setLinearVelocity globule-phys*
                        (.mult (quaternion->direction-vector dir)
                               vel))))

(def valid-input-type (union-impl RGBA BigDecimal))
(defn cannon-timer [app font spatial state nozzle-loc dir velocity mass balls env on-error!]
  (let [*balls* (atom balls)]
    (control-timer spatial 0.5 false
      (fn []
          (let [timer (computation-timer spatial
                        5
                        (fn []
                          (let [balls (value @*balls* env)]
                            (assert! (implements? clojure.lang.Seqable balls))
                            (assert! (not-empty balls))
                            (let [[ball & more-balls] balls]
                              (assert! (implements? valid-input-type ball))
                              [ball more-balls])))
                        ;;Success
                        (fn [[ball more-balls]]
                          (shoot-globule! app font nozzle-loc dir velocity mass ball)
                          (swap! *balls* (constantly more-balls))
                          (let [timer (cannon-timer app font spatial state
                                                    nozzle-loc dir
                                                    velocity mass more-balls
                                                    env
                                                    on-error!)]
                            (swap! state (constantly {:timer timer
                                                      :state :active}))
                            (start! timer)))
                          ;;Failure
                          (fn [error]
                            ;;TODO Properly handle error.
                            (on-error! error)
                            (swap! state (constantly {:state :inactive}))))]
            (swap! state (constantly {:state :active
                                      :timer timer}))
            (start! timer))))))


(defn build-function-cannon [{:keys [x z id direction velocity mass app]}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       ball-font (render-back! (bitmap-font :asset-manager app))
       control (RigidBodyControl. 0.0)
       *state* (atom {:state :inactive})
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
                                  (stop! (:timer state*)))
                                (let [timer (cannon-timer app ball-font cannon *state*
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
                                 (start! timer))))
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
               {:id :velocity  :type :decimal   :label "Velocity"}
               {:id :mass      :type :decimal   :label "Mass"}]
   :prelude `(use '~'tools.level-editor.widgets.function-cannon)
   :build     (fn [[x z] {:keys [id direction velocity mass]}]
                `(do
                   (fn [app#] 
                   (build-function-cannon {:x ~x :z ~z :id  ~id :direction ~direction :velocity ~velocity :mass  ~mass :app app#}))))})

