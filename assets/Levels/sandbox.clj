{:loc [8 10],
 :dir 1.5707963267948966,
 :walls
 #{[2 1] [8 7] [10 9] [6 6] [10 10] [6 7] [10 11] [1 2] [6 8] [9 11]
   [10 12] [1 3] [6 9] [9 12] [10 13] [1 4] [5 9] [6 10] [7 11] [10 14]
   [1 5] [4 9] [6 11] [10 15] [1 6] [3 9] [6 12] [9 15] [1 7] [2 9]
   [6 13] [8 15] [1 8] [6 14] [7 15] [1 9] [6 15] [14 2] [15 3] [13 2]
   [15 4] [12 2] [15 5] [11 2] [15 6] [10 2] [15 7] [9 2] [10 3] [8 2]
   [10 4] [14 8] [7 2] [13 8] [5 1] [6 2] [10 6] [12 8] [4 1] [6 3]
   [10 7] [11 8] [3 1] [6 4] [10 8]},
 :wall-mat "Textures/paper1.jpg",
 :widgets
 [{:name :ball-stereo,
   :answers
   {:z 2,
    :x 2,
    :id "stereo7'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 2,
    :x 3,
    :id "stereo6'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 2,
    :x 4,
    :id "stereo5'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 3,
    :x 2,
    :id "stereo8'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos
    ["stereo1'"
     "stereo2'"
     "stereo3'"
     "stereo4'"
     "stereo5'"
     "stereo6'"
     "stereo7'"
     "stereo8'"],
    :z 3,
    :x 3,
    :muted? false,
    :interactive? true,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance -1,
    :id "Music Box 2"}}
  {:name :ball-stereo,
   :answers
   {:z 3,
    :x 4,
    :id "stereo4'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :text-screen,
   :answers
   {:direction -1.6124389058934852,
    :protocol :cmd-prompt,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A VALUE TO BE PRINTED HERE:",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 3,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Terminal",
    :target-ids []}}
  {:name :color-screen,
   :answers {:z 3, :x 13, :id "", :direction -1.5804114150945991}}
  {:name :ball-stereo,
   :answers
   {:z 4,
    :x 2,
    :id "stereo1'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 4,
    :x 3,
    :id "stereo2'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 4,
    :x 4,
    :id "stereo3'",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :globule-receiver,
   :answers
   {:z 4,
    :x 11,
    :id "",
    :direction 0.02394751692220761,
    :y 1,
    :target-ids [],
    :protocol :open-on-pattern,
    :pattern []}}
  {:name :function-cannon,
   :answers
   {:direction -3.1290933045704317,
    :init-queue "",
    :constraint "",
    :z 4,
    :velocity 75,
    :x 14,
    :transform-id "Transformer",
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon 2"}}
  {:name :sound-barrier,
   :answers
   {:z 5,
    :x 6,
    :id "",
    :direction -3.096425652993974,
    :target-ids ["Music Box 1" "Music Box 2"]}}
  {:name :text-screen,
   :answers
   {:direction -3.1212908747305748,
    :protocol :hold,
    :text "POGGIO INSTITUTE\n================\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 5,
    :x 14,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance -1,
    :id "Transformer",
    :target-ids []}}
  {:name :ball-stereo,
   :answers
   {:z 6,
    :x 2,
    :id "stereo1",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :ball-stereo,
   :answers
   {:z 6,
    :x 3,
    :id "stereo2",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :ball-stereo,
   :answers
   {:z 6,
    :x 4,
    :id "stereo3",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :function-cannon,
   :answers
   {:direction -0.018516402068009044,
    :init-queue "",
    :constraint "",
    :z 6,
    :velocity 75,
    :x 12,
    :transform-id "",
    :mass 0.5,
    :queue? false,
    :distance -1,
    :id "Cannon 1"}}
  {:name :globule-receiver,
   :answers
   {:z 6,
    :x 14,
    :id "Hoop",
    :direction 3.114480654462292,
    :y 2,
    :target-ids ["Cannon 2"],
    :protocol :pass,
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 7,
    :x 2,
    :id "stereo8",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos
    ["stereo1"
     "stereo2"
     "stereo3"
     "stereo4"
     "stereo5"
     "stereo6"
     "stereo7"
     "stereo8"],
    :z 7,
    :x 3,
    :muted? false,
    :interactive? true,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box 1"}}
  {:name :ball-stereo,
   :answers
   {:z 7,
    :x 4,
    :id "stereo4",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :ball-stereo,
   :answers
   {:z 8,
    :x 2,
    :id "stereo7",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :ball-stereo,
   :answers
   {:z 8,
    :x 3,
    :id "stereo6",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :ball-stereo,
   :answers
   {:z 8,
    :x 4,
    :id "stereo5",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :text-screen,
   :answers
   {:direction -1.6031233695506202,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\n<=== MUSIC ROOM\n\n===> CANNON ROOM\n\n/\\  \n|| TERMINAL\n||\n||\n\n||\n|| CELL ROOM\n||\n\\/",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 8,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 3.1326242841789744,
    :protocol :pass,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A RULE TO POPULATE THE CELL WALL",
    :parameter "rule",
    :text-color {:red 127, :green 255, :blue 0},
    :z 12,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: a 3-argument function called rule.\nReturns: Populates the cell wall using rule.",
    :distance 40,
    :id "Cell Wall Transformer",
    :target-ids ["Cell Wall"]}}
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :rule,
    :z 14,
    :init-id "",
    :x 8,
    :precompute? false,
    :rule-id "",
    :target-id "",
    :row-id "",
    :pair-id "",
    :generations-id "",
    :end-protocol :open,
    :id "Cell Wall",
    :rule "(function [left old right] \n   (not old))"}}]}
