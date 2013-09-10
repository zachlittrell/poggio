(ns tools.level-editor.widgets.globule-receiver
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:use [control timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate light geometry material model physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn process-globule! [match? on-match previous conj* globule]
  (detach! (:globule globule))
  (let [results (swap! previous conj* (value (:value globule) {}))]
    (when (match? results)
      (on-match))))


(defn build-globule-receiver [{:keys [x y z id direction target-id pattern app]}]
 (let [loc (Vector3f. (* x 16) (+ -16 y) (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       hoop (model :asset-manager app
;                 :controls [control]
                 :model-name "Models/PowerSource/PowerSource.scene"
                 :name id
                 :local-translation (.subtract loc (.mult dir 
                                                          (Vector3f. 0 0 8)))
                :local-rotation dir
                :controls [control])]
   (doto hoop
     (attach-pog-fn!*  (fn->pog-fn 
                         (partial process-globule!
                                 (comp (partial = [[0.0 0.0 1.0]
                                                   [0.0 1.0 0.0]
                                                   [1.0 0.0 0.0]])
                                       lifo)
                                 #(when-let [target (select app target-id)]
                                    (invoke* (spatial-pog-fn target) [false]))
                                 (atom (ring-buffer 3))
                                 (fn [coll x] (adjoin coll (color->triple x))))
                          "receiver"
                          ["ball"]
                          (docstr [["color" "A colored globule"]]
                            (str "Activates " target-id 
                                 " once it receives a red,"
                                 " green, and blue globule."))
                                       
                                    )))))

(def globule-receiver-template
  {:image (image-pad [100 100]
            (.drawString "◎" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :y  :type :integer :label "Height"}
               {:id :target-id :type :string :label "Target"}
               {:id :pattern :type [:list :string] :label "Matches"}]
   :prelude `(use '~'tools.level-editor.widgets.globule-receiver)
   :build (fn [[x z] {:keys [id direction y target-id pattern]}]
            `(do
               (fn [app#]
               (build-globule-receiver {:x ~x :y ~y :z ~z :id ~id :direction ~direction :target-id ~target-id :pattern ~pattern :app app#}))))})

