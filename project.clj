(defproject poggio "0.3.3-SNAPSHOT"
  :description "FIXME: write description"
  :repositories {"local" ~(str (.toURI (java.io.File. "private_maven")))}
                ;;{"oss-sonatype" "https://oss.sonatype.org/content/repositories/snapshots/"}
;  :main poggio.core
  :omit-source true
  :profiles {:dev {:dependencies 
                   [[lein-autodoc "0.9.0"]
                    [jME3-testdata "3.0.0.20130526-SNAPSHOT"]
                   ;[com.jme3/jME3-testdata "3.0.0.20121220-SNAPSHOT"]
                    ]}}
  :resource-paths ["assets"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.match "0.2.0-alpha9"]
                 [org.clojure/math.combinatorics "0.0.3"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [org.clojars.jmeeks/jfugue "4.0.3"]
                ;;These snapshots are from the JME3 nightly
                 ;;from April 26, 2013.
                [eventbus "3.0.0.20130526-SNAPSHOT"]
                [j-ogg-oggd "3.0.0.20130526-SNAPSHOT"]
                [j-ogg-vorbisd "3.0.0.20130526-SNAPSHOT"]
                [jME3-blender "3.0.0.20130526-SNAPSHOT"]
                [jME3-core "3.0.0.20130526-SNAPSHOT"]
                [jME3-desktop "3.0.0.20130526-SNAPSHOT"]
                [jME3-effects "3.0.0.20130526-SNAPSHOT"]
                [jME3-jbullet "3.0.0.20130526-SNAPSHOT"]
                [jME3-jogg "3.0.0.20130526-SNAPSHOT"]
                [jME3-lwjgl-natives "3.0.0.20130526-SNAPSHOT"]
                [jME3-lwjgl "3.0.0.20130526-SNAPSHOT"]
                [jME3-networking "3.0.0.20130526-SNAPSHOT"]
                [jME3-niftygui "3.0.0.20130526-SNAPSHOT"]
                [jME3-plugins "3.0.0.20130526-SNAPSHOT"]
                [jME3-terrain "3.0.0.20130526-SNAPSHOT"]
                [jbullet "3.0.0.20130526-SNAPSHOT"]
                [jinput "3.0.0.20130526-SNAPSHOT"]
                [lwjgl "3.0.0.20130526-SNAPSHOT"]
                [nifty-default-controls "3.0.0.20130526-SNAPSHOT"]
                [nifty-style-black "3.0.0.20130526-SNAPSHOT"]
                [nifty "3.0.0.20130526-SNAPSHOT"]
                [stack-alloc "3.0.0.20130526-SNAPSHOT"]
                [vecmath "3.0.0.20130526-SNAPSHOT"]
                [xmlpull-xpp3 "3.0.0.20130526-SNAPSHOT"]

              ;;Uncomment these if you are ok with building with older snapshots.
               ;;  [com.jme3/eventbus "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/j-ogg-oggd "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/j-ogg-vorbisd "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-blender "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-core "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-desktop "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-effects "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-jbullet "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-jogg "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-lwjgl-natives "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-lwjgl "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-networking "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-niftygui "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-plugins "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jME3-terrain "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jbullet "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/jinput "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/lwjgl "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/nifty-default-controls "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/nifty-style-black "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/nifty "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/stack-alloc "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/vecmath "3.0.0.20121220-SNAPSHOT"]
               ;;  [com.jme3/xmlpull-xpp3 "3.0.0.20121220-SNAPSHOT"]
                 [instaparse "1.2.4"]
                 [seesaw "1.4.3"]])
