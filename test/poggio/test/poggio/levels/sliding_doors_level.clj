(do
 (do
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.glass-door))
 {:loc [1 4],
  :dir 0.2927130752239971,
  :walls
  #{[2 1] [5 4] [8 7] [4 4] [8 8] [1 1] [4 5] [7 8] [0 1] [3 5] [4 6]
    [7 9] [0 2] [2 5] [4 7] [5 8] [7 10] [0 3] [4 8] [5 9] [0 4] [1 5]
    [5 10] [6 11] [7 2] [6 2] [7 3] [4 1] [5 2] [7 4] [8 5] [3 1] [4 2]
    [8 6]},
  :wall-mat "Textures/Terrain/BrickWall/BrickWall.jpg",
  :widgets
  [(do
    (clojure.core/fn
     [app__9601__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      1
      2
      ""
      0.010637896575541442
      100.0
      0.5
      app__9601__auto__)))
   (do
    (clojure.core/fn
     [app__9649__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      3
      2
      2
      ""
      3.131388926095839
      "door1"
      app__9649__auto__)))
   (do
    (clojure.core/fn
     [app__9718__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      4
      3
      "door1"
      -3.141592653589793
      16.0
      :left
      0.5
      0.0
      app__9718__auto__)))
   (do
    (clojure.core/fn
     [app__9601__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      5
      5
      ""
      -1.5707963267948966
      100.0
      0.5
      app__9601__auto__)))
   (do
    (clojure.core/fn
     [app__9649__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      5
      2
      7
      ""
      1.5707963267948966
      "door2"
      app__9649__auto__)))
   (do
    (clojure.core/fn
     [app__9718__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      6
      8
      "door2"
      1.5707963267948966
      16.0
      :left
      0.5
      1.75
      app__9718__auto__)))]})
