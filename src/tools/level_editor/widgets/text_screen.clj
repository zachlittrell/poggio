(ns tools.level-editor.widgets.text-screen
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.control RigidBodyControl]
           [com.jme3.font Rectangle]
           [com.jme3.math ColorRGBA FastMath Vector3f]
           [com.jme3.scene.shape Box Quad]) 
  (:use [control timer]
        [data coll color ring-buffer quaternion]
        [jme-clj animate bitmap-text geometry material model node physics physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color utilities]
        [seesawx core]))

(defn build-text-screen [{:keys [x z id direction text target-id app]}]
  (try
 (let [loc (Vector3f. (* x 16) -16  (* z 16))
       dir (angle->quaternion (clamp-angle direction) :y)
       control (RigidBodyControl. 0.0)

       screen (geom :shape (Quad. 16 16)
                    :material (material :asset-manager app
                                        :color {"Color" ColorRGBA/Black})
                    :local-translation (Vector3f. -8 0 0.01)
                    
                    )
       text* (bitmap-text :font (bitmap-font :asset-manager app
                                             :font-name "Fonts/DejaVuSansMono.fnt")
                          :text text
                          :size 0.5 
                          :box (Rectangle. -8 16 16 16)
                          :alignment :left
                          :color (ColorRGBA. 0.5 1 0 1)
                          :local-translation (Vector3f. 0 0 0.02))
       node (node*
                    :local-translation (.subtract loc (.mult dir
                                                             (Vector3f. 0 0 8)))
                    :local-rotation dir
                    :children [screen text*])
       ]
   (.updateLogicalState text* 0)
   (if (empty? target-id)
     node
     node))
    
   (catch Exception e
     (.printStackTrace e)))
    )

(def text-screen-template
  {:image (image-pad [100 100]
            (.drawString "¶" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :text  :type [:string :multi-line? true] :label "Text"}
               {:id :target-id :type :string :label "Target"}]
   :prelude `(use '~'tools.level-editor.widgets.text-screen)
   :build (fn [[x z] {:keys [id direction text target-id]}]
            `(do
               (fn [app#]
               (build-text-screen {:x ~x :z ~z :id ~id :direction ~direction :text ~text :target-id ~target-id :app app#}))))})

