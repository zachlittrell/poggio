(ns tools.level-editor.widgets.sound-barrier
  (:import [com.jme3.bullet.util CollisionShapeFactory]
           [com.jme3.bullet.collision.shapes BoxCollisionShape]
           [com.jme3.bullet.control RigidBodyControl GhostControl]
           [com.jme3.math ColorRGBA Vector3f]
           [com.jme3.scene.shape Box])
  (:require [tools.level-editor.widgets.music-box :as music-box])
  (:use [control bindings timer]
        [data coll color object ring-buffer quaternion]
        [jme-clj animate control geometry material model node physics-control selector]
        [nifty-clj popup]
        [poggio.functions core scenegraph color]
        [seesawx core]))

(defn build-sound-barrier [{:keys [x z id direction target-id app]}]
  (let [loc (Vector3f. (* x 16) -8  (* z 16))
        dir (angle->quaternion (clamp-angle direction) :y)
        on-ghost-control (GhostControl. (BoxCollisionShape. (Vector3f. 8 8 0.1)))
        off-ghost-control (GhostControl. (BoxCollisionShape. (Vector3f. 8 8 0.1)))
        door (geom :shape (Box. 7.9 7.9 4)
                   :name id
                   :material (material
                               :asset-manager app
                               :texture {"ColorMap"
                                         (texture :asset-manager app
                                                  :texture-key "Textures/barrier1.png")}))
        on-ghost (node* :local-translation (Vector3f. 0 0 -4.1))
        off-ghost (node* :local-translation (Vector3f. 0 0 4.1))
        node (node* :local-translation loc
                    :local-rotation dir
                    :children [door on-ghost off-ghost])
        toggle-ghost-pog-fn (fn [on?]
                              (fn->pog-fn 
                                (fn [player]
                                  (when (implements? com.jme3.bullet.control.CharacterControl
                                                     player)
                                    (music-box/set-volume!
                                      (select app target-id)
                                      on?)))
                                "on/off"
                                ["player"]
                                "should not see this."))]
    (.addControl on-ghost on-ghost-control)
    (.addControl off-ghost off-ghost-control)
    (attach-pog-fn! on-ghost (toggle-ghost-pog-fn true))
    (attach-pog-fn! off-ghost (toggle-ghost-pog-fn false))
    (transparent! door)
    (-> door
       (.getMaterial)
       (.getAdditionalRenderState)
       (.setFaceCullMode com.jme3.material.RenderState$FaceCullMode/Off))
    node))

(def sound-barrier-template
  {:image (image-pad [100 100]
            (.drawString "♯" 49 49))
   :questions [{:id :id :type :string :label "ID"}
               {:id :direction :type :direction :label "Direction"}
               {:id :target-id :type :string :label "Target"}]
   :build build-sound-barrier
   })

