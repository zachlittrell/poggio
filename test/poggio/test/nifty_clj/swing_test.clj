(ns poggio.test.nifty-clj.swing-test
  (:use [jme-clj swing-jme]
        [seesaw core])
  (:import [com.jme3.app SimpleApplication]))

(defn make-app []
  (proxy [SimpleApplication] []
    (simpleInitApp [] 
      (doto (.getGuiNode this)
        (.detachAllChildren)
        (.attachChild (doto
                        (swing-component->geom (.getAssetManager this)
                            (scrollable (text :multi-line? true
                                              :text "HEY\n\n\n\n\nBYE"))
                            126 64)
                        (.setLocalTranslation 300 300 -1)))))))

(defn -main [& args]
  (doto (make-app)
    (.start)))
