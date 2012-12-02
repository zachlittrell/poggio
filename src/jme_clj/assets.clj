(ns jme-clj.assets
  "Functions for handling assets in JME."
  (:import [com.jme3.app SimpleApplication]
           [com.jme3.asset AssetManager]))

(defprotocol AssetManagerProvider
  (asset-manager [provider]))

(extend-type SimpleApplication
  AssetManagerProvider
  (asset-manager [app] (.getAssetManager app)))

(extend AssetManager
  AssetManagerProvider
  {:asset-manager identity})

