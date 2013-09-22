(ns tools.level-editor.widgets.globule-receiver
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str])
  (:use [control io timer]
        [data coll color object ring-buffer string quaternion]
        [jme-clj animate light geometry material model physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn process-globule! [hoop match? on-match previous conj* globule]
  (detach! (:globule globule))
  (let [val (value (:value globule) {})
        results (swap! previous conj* (value (:value globule) {}))]
    (when (implements? ColorRGBA val)
      (.setColor (.getMaterial hoop) "Color" val) )
    (when (match? results)
      (on-match))))

(defn pass-globule! [app hoop target-id  globule]
  (detach! (:globule globule))
  (when-let [target (select app target-id)]
  (let [val (value (:value globule) {})]
     (when (implements? ColorRGBA val)
      (.setColor (.getMaterial hoop) "Color" val) )
     (invoke*  (spatial-pog-fn target) 
               [nil (:on-error! globule) 
                        (list val)]))))


(defn str->encoding [^String s]
  (condp re-matches s
    #"-?(\d+)(.\d+)?" [:num (bigdec s)]
    #"red" [:color 1.0 0.0 0.0]
    #"green" [:color 0.0 1.0 0.0]
    #"blue" [:color 0.0 0.0 1.0]))

(defprotocol Encodable
  (encode [o]))

(extend-protocol Encodable
  Number
  (encode [d] [:num d])
  ColorRGBA
  (encode [c] [:color (red c) (green c) (blue c)]))

(defn build-globule-receiver [{:keys [x y z id direction target-id  protocol pattern app]}]
 (let [loc (Vector3f. (* x 16) (+ -16 y) (* z 16))
       dir (angle->quaternion direction :y)
       control (RigidBodyControl. 0.0)
       hoop (model :asset-manager app
                 :model-name "Models/PowerSource/PowerSource.scene"
                 :name id
                 :local-translation (.subtract loc (.mult dir 
                                                          (Vector3f. 0 0 8)))
                :local-rotation dir
                :controls [control])
       hoop-ball (.getChild (.getChild hoop "Sphere") "Sphere") ]
   (.setMaterial hoop-ball (material :asset-manager app
                                     :color {"Color" ColorRGBA/White}))
   (doto hoop
     (attach-pog-fn!*  
       (case protocol
         :pass
           (fn->pog-fn
             (partial pass-globule! app hoop-ball target-id)
             "receiver"
             ["ball"]
             (docstr [["globule" "a globule"]]
                 (str "Passes globules to " target-id)))
         :open
           (fn->pog-fn 
            (partial process-globule!
                     hoop-ball
                    (comp (partial = (map str->encoding
                                          (rseq pattern)))
                          lifo)
                    #(when-let [target (select app target-id)]
                       (invoke* (spatial-pog-fn target) [false]))
                    (atom (ring-buffer (count pattern)))
                    (fn [coll x] 
                      (adjoin coll (encode x))))
             "receiver"
             ["ball"]
             (docstr [["globule" "a globule"]]
               (format "Activates %s once it receives %s."
                   target-id
                   (str/join ", " pattern)))
                          
                       ))))))

(def globule-receiver-template
  {:image (image-pad [100 100]
            (.drawString "◎" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :y  :type :integer :label "Height"}
               {:id :target-id :type :string :label "Target"}
               {:id :protocol :type [:choice
                                     :model [:open
                                             :pass]] :label "Protocol"}
               {:id :pattern :type [:list :type :string] :label "Matches"}]
   :build build-globule-receiver
   ;:prelude `(use '~'tools.level-editor.widgets.globule-receiver)
   
  ; :build (fn [[x z] {:keys [id direction y target-id pattern protocol]}]
  ;          `(do
  ;             (fn [app#]
  ;             (build-globule-receiver {:x ~x :y ~y :z ~z :id ~id :direction ~direction :target-id ~target-id :pattern ~pattern :app app# :protocol ~protocol}))))
   })

