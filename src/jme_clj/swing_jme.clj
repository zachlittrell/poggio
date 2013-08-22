(ns jme-clj.swing-jme
  (:use [jme-clj geometry material])
  (:import [com.jme3.export Savable]
           [com.jme3.scene Spatial]
           [com.jme3.scene.shape Quad]
           [jme_clj PaintableTexture]
           [javax.swing JComponent]))

(defrecord SwingPainter [paintable-texture component]
  Savable)

(defn is-swing-spatial? [^Spatial spatial]
  (not (nil? (.getUserData spatial "_swing_painter"))))

(defn swing-component->geom 
  ([asset-manager ^JComponent component]
   (swing-component->geom asset-manager component (.getWidth component)
                                                  (.getHeight component)))
  ([asset-manager ^JComponent component width height]
    (let [texture (PaintableTexture. asset-manager width height)
          geometry (geom :shape (Quad. width height)
                         :material (.getMaterial texture))]
      (.setUserData geometry "_swing_painter" 
                    (SwingPainter. texture component))
      (.printAll texture component)
      geometry)))

(defn update-swing-patial! [^Spatial spatial]
  (let [swing-painter (.getUserData spatial "_swing_painter")
        texture ^PaintableTexture (:paintable-texture swing-painter)]
    (.printAll ^JComponent (:component swing-painter)
                           (.getGraphics texture))
   (.updateTexture texture)))
