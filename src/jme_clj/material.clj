(ns jme-clj.material
  (:use [control.defutilities :only [def-opts-constructor]]
        [data enum])
  (:import [com.jme3.asset TextureKey]
           [com.jme3.material Material]
           [com.jme3.texture Texture Texture$WrapMode]))

(def-opts-constructor material
  [:setter]
  {:asset-manager nil
   :def-name      ""}
  `(Material. ~'asset-manager ~'def-name)
  {:asset-manager [:no-op]
   :def-name      [:no-op]
   :texture       [:map-do-seq
                   :replace [#"^(.*)$" "set-texture"]]})

(def-opts-constructor texture-key 
  [:setter]
  {:name ""
   :flip-y false}
  `(TextureKey. ~'name ~'flip-y)
  {:name [:no-op]
   :flip-y [:no-op]})

(def-enum-keyword-map keyword->wrap-mode Texture$WrapMode)

(def-opts-constructor texture
  [:setter]
  {:asset-manager nil
   :texture-key   nil}
  `(.loadTexture ~'asset-manager ~'texture-key)
  {:asset-manager [:no-op]
   :texture-key   [:no-op]
   :wrap          [:setter
                   :thread-in `(keyword->wrap-mode)]})

(defn textured-material [asset-manager texture-key]
  (material :asset-manager asset-manager
            :def-name "Common/MatDefs/Misc/Unshaded.j3md"
            :texture {"ColorMap" (texture :asset-manager asset-manager
                                           :texture-key texture-key)}))
