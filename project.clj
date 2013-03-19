(defproject poggio "1.0.0-SNAPSHOT"
  :main poggio.core
  :description "FIXME: write description"
  :repositories {"oss-sonatype" "https://oss.sonatype.org/content/repositories/snapshots/"}
  :profiles {:dev {:dependencies 
                   [[lein-autodoc "0.9.0"]
                    [seesaw "1.4.2"]
                   [com.jme3/jME3-testdata "3.0.0.20121220-SNAPSHOT"]]}}
  :resource-paths ["assets"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.match "0.2.0-alpha9"]
                 [org.clojure/tools.macro "0.1.1"]
                 [com.jme3/eventbus "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/j-ogg-oggd "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/j-ogg-vorbisd "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-blender "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-core "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-desktop "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-effects "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-jbullet "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-jogg "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-lwjgl-natives "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-lwjgl "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-networking "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-niftygui "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-plugins "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jME3-terrain "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jbullet "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/jinput "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/lwjgl "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/nifty-default-controls "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/nifty-style-black "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/nifty "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/stack-alloc "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/vecmath "3.0.0.20121220-SNAPSHOT"]
                 [com.jme3/xmlpull-xpp3 "3.0.0.20121220-SNAPSHOT"]])
