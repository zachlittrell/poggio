(ns tools.level-editor.templates
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box])
  (:use [data coll color ring-buffer quaternion]
        [jme-clj animate geometry material model physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color]
        [seesawx core]
        [tools.level-editor actions]))
;

(defn build-glass-door [x z id direction app]
  (let [loc (Vector3f. (* x 16) -8  (* z 16))
        dir (angle->quaternion direction :y)
        control (RigidBodyControl. 0.0)
        door (geom :shape (Box. 8 8 8)
                   :controls [control]
                   :material (material :asset-manager app
                                       :texture {"ColorMap"
                                                 (texture :asset-manager app
                                                          :texture-key "Textures/water1.png")}))]
;                                       :color {"Color" 
;                                               (ColorRGBA. 0.85 1 0.94 0.5)}))]
    (pog-fn-node :children [(transparent! door)]
                 :name id
                 :pog-fn (fn->pog-fn (fn [open?]
                                      (visibility! app (.getParent door) open?))
                                     ""
                                     ["open?"])
                 :local-translation loc
                 :local-rotation dir
                 :controls [control])))

(def glass-door-template
  {:image (image-pad [100 100]
            (.drawString "◫" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :open? :type :boolean :label "Open"}]
   :prelude `(use '~'tools.level-editor.templates)
   :build (fn [[x z] {:keys [id direction open?]}]
           `(do
                 (fn [app#] 
                 (build-glass-door ~x ~z ~id ~direction app#))))})

(def colors (atom (flatten (repeat [(value blue*) (value red*) (value green*)]))))

(defn build-function-cannon [x z id direction velocity mass app]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       cannon (model :asset-manager app
                :controls [control]
                    :model-name "Models/Laser/Laser.scene")]
   (pog-fn-node :name id
                :local-translation loc
                :local-rotation dir
                :controls [control]
                :pog-fn (fn->pog-fn (fn []
                                      (shoot-globule!
                                        app
                                        (.add loc 
                                              (.mult dir (Vector3f. 0 4 2.1)))
                                        dir
                                        (float velocity)
                                        (float mass)
                                        (first (swap! colors rest))))
                                     "" 
                                     [])
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
   :prelude `(use '~'tools.level-editor.templates)
   :build     (fn [[x z] {:keys [id direction velocity mass]}]
                `(do
                   (fn [app#] 
                   (build-function-cannon ~x ~z ~id ~direction ~velocity ~mass app#))))})

(defn build-globule-receiver [x y z id direction target-id app]
 (let [loc (Vector3f. (* x 16) (+ -16 y) (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       hoop (model :asset-manager app
                 :controls [control]
                 :model-name "Models/PowerSource/PowerSource.scene")]
   (pog-fn-node :name id
                :local-translation (.subtract loc (.mult dir 
                                                          (Vector3f. 0 0 8)))
                :local-rotation dir
                :controls [control]
                :pog-fn (basic-pog-fn  ["ball"]
                                      (partial apply process-globule!
                                              (comp (partial = [[0.0 0.0 1.0]
                                                                [0.0 1.0 0.0]
                                                                [1.0 0.0 0.0]])
                                                    lifo)
                                              #(invoke 
                                                 (select app target-id)
                                                 [false])
                                              (atom (ring-buffer 3))))
                :children [hoop])))


(def globule-receiver-template
  {:image (image-pad [100 100]
            (.drawString "◎" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :y  :type :integer :label "Height"}
               {:id :target-id :type :string :label "Target"}]
   :prelude `(use '~'tools.level-editor.templates)
   :build (fn [[x z] {:keys [id direction y target-id]}]
            `(do
               (fn [app#]
               (build-globule-receiver ~x ~y ~z ~id ~direction ~target-id app#))))})

