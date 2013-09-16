(do
 (do
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen))
 {:loc [7 20],
  :dir 1.5707963267948966,
  :walls
  #{[17 17] [18 18] [16 17] [18 19] [15 17] [17 19] [14 17] [16 19]
    [13 17] [16 20] [12 17] [13 18] [11 17] [15 21] [10 17] [14 21]
    [9 17] [11 19] [13 21] [8 17] [9 18] [11 20] [12 21] [7 17] [9 19]
    [11 21] [6 17] [9 20] [11 22] [10 22] [5 18] [9 22] [5 19] [7 21]
    [8 22] [5 20] [6 21]},
  :wall-mat "Textures/paper1.jpg",
  :widgets
  [(do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.6017143063579393,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nPOGGIO\n\nPROGRAMMED AND DESIGNED BY \nZACH LITTRELL\n\nTHANK YOU FOR PLAYING!\n",
       :parameter "message",
       :z 18,
       :x 7,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.120319267565732,
       :protocol :none,
       :text "TESTERS\n================\n\nYOUR NAME HERE!",
       :parameter "message",
       :z 18,
       :x 12,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1100__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.0940095503128098,
       :movement :up,
       :speed 5.0,
       :z 18,
       :x 16,
       :app app__1100__auto__,
       :time 0.0,
       :distance 8.0,
       :id "door"})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 0.03527947359359127,
       :protocol :none,
       :text "LIBRARIES\n================\n\ninstaparse\n\nseesaw",
       :parameter "message",
       :z 19,
       :x 6,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.087101197349062,
       :protocol :none,
       :text
       "TECHNOLOGY\n================\n\nJMonkeyEngine - GAME ENGINE\nClojure - PROGRAMMING LANGUAGE\nVim - EDITOR\nLeiningen - BUILD TOOL\n",
       :parameter "message",
       :z 19,
       :x 8,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 0.013697773372865788,
       :protocol :none,
       :text
       "TEXTURES & Models\n================\n\nSEE SOURCE FOR ATTRIBUTION.",
       :parameter "message",
       :z 19,
       :x 10,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.1183410307793302,
       :protocol :open,
       :text
       "POGGIO INSTITUTE\n================\n\nTO EXIT, SEND true.\n",
       :parameter "message",
       :z 19,
       :x 15,
       :success-text "GOOD BYE.",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? true,
       :target-id "door",
       :success? "message",
       :error-text "You need to send true to unlock the exit.",
       :app app__1784__auto__,
       :docstring
       "Takes: a boolean b.\nDoes: Opens the door if b is true.",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.0750244898139694,
       :protocol :none,
       :text "EXIT",
       :parameter "message",
       :z 20,
       :x 15,
       :success-text "",
       :font-size 2.0,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1784__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "MUSIC\n================\n\n06 Ghosts I - Nine Inch Nails",
       :parameter "message",
       :z 21,
       :x 9,
       :success-text "",
       :font-size 0.6,
       :transform-param "x",
       :transform "x",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1784__auto__,
       :docstring "",
       :id ""})))]})
