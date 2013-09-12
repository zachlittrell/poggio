(do
 (do
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door))
 {:loc [5 3],
  :dir 1.5707963267948966,
  :walls
  #{[3 2] [3 3] [7 7] [3 4] [6 7] [3 5] [6 8] [3 6] [4 7] [6 9] [4 8]
    [4 9] [5 10] [8 1] [7 1] [8 2] [6 1] [8 3] [5 1] [8 4] [4 1] [8 5]
    [3 1] [8 6]},
  :wall-mat "Textures/paper1.jpg",
  :widgets
  [(do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5208379310729538,
       :text
       "POGGIO\n\nPROGRAMMED BY\nZACH LITTRELL\n\nTHANKS FOR PLAYING!",
       :font-size 0.5,
       :parameter "message",
       :z 2,
       :x 5,
       :success-text "",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__9769__auto__,
       :docstring "",
       :id "Credit To Me"})))
   (do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.513715544388632,
       :text
       "TECHNOLOGY\n==========\nGAME ENGINE - jMonkeyEngine\n\nPROGRAMMING LANGUAGE - Clojure\n\nBUILD TOOL - Leiningen\n\nEDITOR - Vim",
       :parameter "message",
       :font-size 0.5,
       :z 2,
       :x 7,
       :success-text "",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__9769__auto__,
       :docstring "",
       :id "Credit for Technology"})))
   (do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.0890095919788516,
       :text "LIBRARIES\n=========\ninstaparse\nseesaw",
       :parameter "message",
       :font-size 0.5,
       :z 3,
       :x 7,
       :success-text "",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__9769__auto__,
       :docstring "",
       :id "Credit For Libraries"})))
   (do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -0.11710874456686428,
       :text "TESTERS\n=======\nYou're name here!",
       :parameter "message",
       :font-size 0.5,
       :z 4,
       :x 4,
       :success-text "",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__9769__auto__,
       :docstring "",
       :id "Credit for Testers"})))
   (do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :text
       "POGGIO INSTITUTE\n=================\n\nEXIT\n\n<=========\n<=========",
       :font-size 0.5,
       :parameter "message",
       :z 6,
       :x 4,
       :success-text "",
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__9769__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__9769__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :text "POGGIO INSTITUTE\n=============\n\nTO EXIT, SEND true",
       :font-size 0.5,
       :parameter "message",
       :z 6,
       :x 6,
       :success-text "\nGOOD BYE.    ",
       :end-level? true,
       :target-id "door",
       :success? "message",
       :error-text "The door only unlocks if you send true.",
       :app app__9769__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__8732__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction 1.5707963267948966,
       :movement :up,
       :speed 4.0,
       :z 7,
       :x 5,
       :app app__8732__auto__,
       :time 0.0,
       :distance 8.0,
       :id "door"})))]})
