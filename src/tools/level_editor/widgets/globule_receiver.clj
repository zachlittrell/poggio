(ns tools.level-editor.widgets.globule-receiver
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box])
  (:use [data coll color ring-buffer quaternion]
        [jme-clj animate geometry material model physics physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color]
        [seesawx core]))

(defn process-globule! [match? on-match previous globule]
  (detach! globule)
  (let [results (swap! previous adjoin (value globule))]
    (when (match? results)
      (on-match))))


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
                                              #(when-let [target (select app target-id)]
                                                 (invoke target [false]))
                                              (atom (ring-buffer 3))))
                :children [hoop])))


(def globule-receiver-template
  {:image (image-pad [100 100]
            (.drawString "◎" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :y  :type :integer :label "Height"}
               {:id :target-id :type :string :label "Target"}]
   :prelude `(use '~'tools.level-editor.widgets.globule-receiver)
   :build (fn [[x z] {:keys [id direction y target-id]}]
            `(do
               (fn [app#]
               (build-globule-receiver ~x ~y ~z ~id ~direction ~target-id app#))))})

