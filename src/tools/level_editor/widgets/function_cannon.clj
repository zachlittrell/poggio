(ns tools.level-editor.widgets.function-cannon
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.collision.shapes SphereCollisionShape]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box Sphere])
  (:require [clojure.string :as str])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate bitmap-text control geometry material model node physics physics-control selector spatial transform]
        [nifty-clj popup]
        [poggio.functions core parser modules scenegraph color utilities]
        [seesawx core]
        [tools.level-editor.widgets utilities]))

(def globule-shape (Sphere. 32 32 (float 0.4)))
(def num-ball-color (ColorRGBA. 1 1 1 0.5))
(def num-font-color (ColorRGBA. 0 0 0 0.5))

;;TODO move this stuff into utilities
(defprotocol Ballable
  (modify-ball! [val app font ball-node ball]))

(extend-protocol Ballable
  ColorRGBA
  (modify-ball! [color app ball-font ball-node ball]
    (.setMaterial ball 
       (material :asset-manager app
                 :color {"Color" color})))
  Boolean
  (modify-ball! [boolean app ball-font ball-node ball]
    (modify-ball! (str boolean) app ball-font ball-node ball))
  Number
  (modify-ball! [num app ball-font ball-node ball]
    (modify-ball! (str num) app ball-font ball-node ball))
  String
  (modify-ball! [num-str app ball-font ball-node ball]
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
                  ))

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


(defn build-function-cannon [{:keys [x z id direction velocity mass app
                                     queue?
                                     init-queue
                                     constraint
                                     transform-id
                                     on-error!
                                     distance]
                              :or 
                              {distance 24}}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       ball-font (render-back! (bitmap-font :asset-manager app))
       control (RigidBodyControl. 0.0)
       valid-input-type (cond (empty? constraint) (union-impl RGBA Number Boolean)
                           (= constraint "color") RGBA
                           (= constraint "number") Number
                           (= constraint "boolean") Boolean)
       cannon (model :asset-manager app
                :model-name (if queue?
                              "Models/Laser/Laser_inactive.scene"
                              "Models/Laser/Laser.scene")
                :name id
                :local-translation loc
                :local-rotation dir
                     :controls [control])]
   (doto cannon
     (attach-pog-fn!*
       (do-list-pog-fn :spatial cannon
                       :app app
                       :on-value! (partial shoot-globule!
                                           app
                                           on-error!
                                           ball-font
                                           (.add loc
                                              (.mult dir
                                                     (Vector3f. 0 4 2.1)))
                                           dir
                                           (float velocity)
                                           (float mass))
                       :init-wait-time 0.5
                       :on-error! on-error!
                       :queue? queue?
                       :transformer-id transform-id
                       :valid-input-type valid-input-type
                       :queue-init init-queue
                       :distance distance
                       :param "values")))
   (start-do-list!? cannon queue? init-queue)
   cannon))

 
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
               {:id :init-queue :type [:string :multi-line? true]
                                :label "Init Queue"}
               {:id :constraint :type :string  :label "Constraint"}
               {:id :distance :type [:integer
                                     :init 24] :label "Distance"}]
   :build build-function-cannon
   })

