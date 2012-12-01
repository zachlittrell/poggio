(ns jme-clj.assets
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.asset AssetManager]))

(defprotocol AssetManagerOwner
  (asset-manager [owner]))

(extend-type SimpleApplication
  AssetManagerOwner
  (asset-manager [app] (.getAssetManager app)))

(extend AssetManager
  AssetManagerOwner
  {:asset-manager identity})

