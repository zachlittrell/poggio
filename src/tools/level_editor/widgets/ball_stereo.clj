(ns tools.level-editor.widgets.ball-stereo
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.math FastMath ColorRGBA Vector3f]
           [com.jme3.scene Geometry]
           [com.jme3.scene.shape Box])
  (:require [clojure.string :as str])
  (:use [control io timer]
        [data coll color object ring-buffer string quaternion]
        [jme-clj animate light geometry material model physics physics-control selector spatial]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn build-ball-stereo [{:keys [x z id target-id  protocol pattern app]}]
 (let [loc (Vector3f. (* x 16) -16 (* z 16))
       dir (angle->quaternion FastMath/PI :x)
       control (RigidBodyControl. 0.0)
       hoop (model :asset-manager app
                 :model-name "Models/PowerSource/PowerSource.scene"
                 :name id
                 :local-translation loc
                  ; (.subtract loc (.mult dir 
                  ;                                        (Vector3f. 0 0 8)))
                :local-rotation dir)
       hoop-ball (.getChild (.getChild hoop "Sphere") "Sphere") ]
   (.scale hoop 1.5 1.5 1.5)
   (.addControl hoop control)
   (.setMaterial hoop-ball (material :asset-manager app
                                     :color {"Color" ColorRGBA/White}))
   (doto hoop))) 
   ;;(doto hoop
   ;;  (attach-pog-fn!*  
   ;;    (case protocol
   ;;      :pass
   ;;        (fn->pog-fn
   ;;          (partial pass-globule! app hoop-ball target-id)
   ;;          "receiver"
   ;;          ["ball"]
   ;;          (docstr [["globule" "a globule"]]
   ;;              (str "Passes globules to " target-id)))
   ;;      :open
   ;;        (fn->pog-fn 
   ;;         (partial process-globule!
   ;;                  hoop-ball
   ;;                 (comp (partial = (map str->encoding
   ;;                                       (rseq pattern)))
   ;;                       lifo)
   ;;                 #(when-let [target (select app target-id)]
   ;;                    (invoke* (spatial-pog-fn target) [false]))
   ;;                 (atom (ring-buffer (count pattern)))
   ;;                 (fn [coll x] 
   ;;                   (adjoin coll (encode x))))
   ;;          "receiver"
   ;;          ["ball"]
   ;;          (docstr [["globule" "a globule"]]
   ;;            (format "Activates %s once it receives %s."
   ;;                target-id
   ;;                (str/join ", " pattern)))
   ;;                       
   ;;                    ))))))

(def ball-stereo-template
  {:image (image-pad [100 100]
            (.drawString "!" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :target-id :type :string :label "Target"}]
   :build build-ball-stereo
   })

