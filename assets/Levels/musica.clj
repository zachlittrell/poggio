{:loc [1 3],
 :dir -1.0663550349262252,
 :walls
 #{[6 5] [18 17] [20 19] [23 22] [2 2] [3 3] [5 5] [10 10] [18 18]
   [19 19] [23 23] [3 4] [4 5] [6 7] [7 8] [9 10] [20 21] [1 2] [3 5]
   [7 9] [8 10] [17 19] [19 21] [20 22] [22 24] [4 7] [7 10] [12 15]
   [13 16] [20 23] [22 25] [0 3] [3 7] [11 15] [13 17] [15 19] [16 20]
   [18 22] [20 24] [22 26] [0 4] [2 7] [3 8] [6 11] [10 15] [13 18]
   [14 19] [16 21] [17 22] [19 24] [21 26] [0 5] [1 6] [3 9] [5 11]
   [9 15] [11 17] [13 19] [16 22] [17 23] [20 26] [0 6] [3 10] [4 11]
   [8 15] [10 17] [11 18] [14 21] [17 24] [18 25] [10 18] [11 19]
   [13 21] [14 22] [17 25] [19 27] [7 16] [11 20] [12 21] [13 22]
   [18 27] [6 16] [13 23] [17 27] [6 17] [10 21] [13 24] [14 25]
   [16 27] [6 18] [9 21] [13 25] [14 26] [15 27] [6 19] [9 22] [6 20]
   [7 21] [9 23] [13 27] [6 21] [7 22] [9 24] [12 27] [7 23] [9 25]
   [10 26] [12 28] [7 24] [10 27] [12 29] [7 25] [10 28] [8 27] [11 30]
   [6 26] [9 29] [18 6] [6 27] [10 31] [17 6] [18 7] [19 8] [20 9]
   [6 28] [7 29] [9 31] [16 6] [18 8] [20 10] [7 30] [8 31] [13 4]
   [15 6] [20 11] [7 31] [12 4] [13 5] [14 6] [16 8] [20 12] [11 4]
   [13 6] [15 8] [23 16] [10 4] [14 8] [15 9] [19 13] [20 14] [21 15]
   [22 16] [23 17] [9 4] [13 8] [15 10] [19 14] [21 16] [23 18] [9 5]
   [12 8] [15 11] [17 13] [23 19] [9 6] [15 12] [16 13] [17 14] [19 16]
   [23 20] [7 5] [8 6] [9 7] [11 9] [17 15] [18 16] [23 21]},
 :wall-mat "Textures/tile1.png",
 :widgets
 [{:name :text-screen,
   :answers
   {:direction 3.103696113750955,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\n\"If music be the food of love, play on\"\n\n~ Someone either overly enthusiastic about music, food, love, or all three.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 5,
    :x 2,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "",
    :target-ids []}}
  {:name :globule-receiver,
   :answers
   {:z 5,
    :x 10,
    :id "Hoop Uno",
    :direction -1.5388922623098742,
    :y 1,
    :target-ids ["Door Uno-1"],
    :protocol :open-on-pattern,
    :pattern ["red"]}}
  {:name :globule-receiver,
   :answers
   {:z 5,
    :x 11,
    :id "Hoop Dos",
    :direction -1.5388922623098742,
    :y 1,
    :target-ids ["Door Uno-2"],
    :protocol :open-on-pattern,
    :pattern ["green"]}}
  {:name :globule-receiver,
   :answers
   {:z 5,
    :x 12,
    :id "Hoop Tres",
    :direction -1.5388922623098742,
    :y 1,
    :target-ids ["Door Uno-3"],
    :protocol :open-on-pattern,
    :pattern ["blue"]}}
  {:name :sound-barrier,
   :answers
   {:z 6,
    :x 6,
    :id "",
    :direction -3.1328592511813667,
    :target-ids ["hmb"]}}
  {:name :text-screen,
   :answers
   {:direction 3.097568254151026,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nA NOTE TO EMPLOYEES:\n\nSOUND BARRIERS HAD TO BE IMPLEMENTED DUE TO SOME ABUSE OF THE MUSIC BOXES.\n\nTHE MUSIC BOXES ARE A PRIVILEGE, NOT A RIGHT TO ANNOY THE JANITORS.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 6,
    :x 7,
    :success-text "",
    :font-size 0.6,
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
   {:direction -0.0,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nTHE FIRST CANNON SPITS\n\n[green red blue]\n\nTHE SECOND CANNON SPITS\n\n[blue green red]\n\nTHE THIRD CANNON SPITS\n\n[red blue green]",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 6,
    :x 10,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 7,
    :x 5,
    :id "",
    :direction 1.5707963267948966,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat [green red blue])",
    :constraint "",
    :z 7,
    :velocity 75,
    :x 10,
    :transform-id "Transformer Uno",
    :interactive? false,
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon Uno"}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat [blue green red])",
    :constraint "",
    :z 7,
    :velocity 75,
    :x 11,
    :transform-id "Transformer Uno",
    :interactive? false,
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon Dos"}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat [red blue green])",
    :constraint "",
    :z 7,
    :velocity 75,
    :x 12,
    :transform-id "Transformer Uno",
    :interactive? true,
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon Tres"}}
  {:name :glass-door,
   :answers
   {:z 7,
    :x 13,
    :id "Door Uno-1",
    :direction -3.1193740882630743,
    :distance 8.0,
    :movement :down,
    :speed 2.0,
    :time 3.0}}
  {:name :glass-door,
   :answers
   {:z 7,
    :x 14,
    :id "Door Uno-2",
    :direction -3.1193740882630743,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 3.0}}
  {:name :glass-door,
   :answers
   {:z 7,
    :x 15,
    :id "Door Uno-3",
    :direction -3.1193740882630743,
    :distance 8.0,
    :movement :right,
    :speed 2.0,
    :time 3.0}}
  {:name :text-screen,
   :answers
   {:direction -3.141592653589793,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT TAKES A LIST AND RETURNS THE SECOND ELEMENT.\n\n(fn [3 4 5]) -- RETURNS 4\n\n",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 8,
    :x 11,
    :success-text "",
    :font-size 0.6,
    :transform "head",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 1-argument function called fn.\nReturns: Modifies what the cannons spit using fn.",
    :distance 40,
    :id "Transformer Uno",
    :target-ids []}}
  {:name :sound-barrier,
   :answers
   {:z 8,
    :x 17,
    :id "",
    :direction -1.6321228379352621,
    :target-ids ["Music Box Uno"]}}
  {:name :ball-stereo,
   :answers
   {:z 9,
    :x 4,
    :id "hs3",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["hs1" "hs2" "hs3"],
    :z 9,
    :x 5,
    :muted? false,
    :interactive? true,
    :queue-init
    "(cycle \n  [(rest 1)\n    e d c d e e e \n    (rest 0.25)\n    d d d\n    (rest 0.25)\n    e g g\n    (rest 0.25)\n    e d c d e e e e d d e d c])",
    :queue? true,
    :transformer-id "",
    :distance 24,
    :id "hmb"}}
  {:name :ball-stereo,
   :answers
   {:z 9,
    :x 6,
    :id "hs1",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :text-screen,
   :answers
   {:direction 3.1336246947282715,
    :protocol :menus,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND THE NUMBER OF THE CHOICE YOU WANT TO VIEW",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 9,
    :x 10,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "{:start 0,\n 0\n {:label\n  \"THE FOLLOWING FUNCTIONS CAN PICK APART LISTS\",\n  :states\n  [{:state 1, :label \"head\"}\n   {:state 2, :label \"tail\"}]},\n 1\n {:label\n  \"head\\n\\nRETURNS THE FIRST ELEMENT OF A LIST.\\n\\n(head [1 2 3]) -- RETURNS 1.\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 2\n {:label\n  \"tail\\n\\nRETURNS ALL ELEMENTS OF A LIST EXCEPT THE FIRST.\\n\\n(tail [1 2 3]) -- RETURNS [2 3].\",\n  :states [{:state 0, :label \"Go Back\"}]}\n}\n",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Help Menu List",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -0.02720417139721311,
    :protocol :none,
    :text
    "PRE-DEFINED NOTES\n================\n\nc - (note 60 0.25)\nd - (note 62 0.25)\ne - (note 64 0.25)\nf - (note 65 0.25)\ng - (note 67 0.25)\na - (note 69 0.25)\nb - (note 71 0.25)\nhigh-c (note 72 0.25)",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 9,
    :x 16,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "",
    :target-ids []}}
  {:name :ball-stereo,
   :answers
   {:z 9,
    :x 19,
    :id "Stereo Uno-1",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 10,
    :x 5,
    :id "hs2",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :text-screen,
   :answers
   {:direction -0.0,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU CAN SEND DIRECTLY TO THE MUSIC BOX A LIST OF NOTES.\n\nA NOTE TAKES IN A PITCH (AN INTEGER 0-127)\n\nAND A DURATION (A DECIMAL BETWEEN 0.0 AND 1.0).\n\nYOU CAN ALSO HAVE RESTS, WHICH ONLY TAKE DURATIONS.\n\n\nTRY SENDING\n\n(cycle [c c d e c e d (rest 1)])\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 10,
    :x 16,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "",
    :target-ids []}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos
    ["Stereo Uno-1" "Stereo Uno-2" "Stereo Uno-3" "Stereo Uno-4"],
    :z 10,
    :x 17,
    :muted? false,
    :interactive? true,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance -1,
    :id "Music Box Uno"}}
  {:name :ball-stereo,
   :answers
   {:z 10,
    :x 18,
    :id "Stereo Uno-2",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 11,
    :x 17,
    :id "Stereo Uno-3",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :ball-stereo,
   :answers
   {:z 12,
    :x 16,
    :id "Stereo Uno-4",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern []}}
  {:name :sound-barrier,
   :answers
   {:z 13,
    :x 18,
    :id "",
    :direction 1.6385934944582867,
    :target-ids ["Music Box Uno"]}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\n\nBOOO!\n\nI WANNA HEAR MY JAM!!!",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 15,
    :x 18,
    :success-text "",
    :font-size 0.6,
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
   {:direction 0.01212061854209015,
    :protocol :pass-with,
    :text
    "POGGIO INSTITUTE\n================\nSEND A FUNCTION THAT TAKES A LIST OF LIST OF INTEGERS, FILTERS OUT INTEGERS OUTSIDE THE RANGE OF 0-127, AND CONVERTS EACH INTEGER INTO A NOTE.\n\nSO, EXAMPLE:\n\n(fn [ [60 62] [-1 64] ]) \nRETURNS [[c d] [e]]\n",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 16,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :split? true,
    :transform
    "[[50 50 -1 52 50 48]\n  [62 62 64 62 60]\n  [74 1000 74 76 74 72]]",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 1-argument function fn.\nReturns: Modifies a given list of list of integers with fn and sends it to the music boxes.",
    :distance 40,
    :id "Transformer Cuatro",
    :target-ids
    ["Music Box Cuatro" "Music Box Cinco" "Music Box Seis"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5507187037813726,
    :protocol :menus,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND THE NUMBER OF THE CHOICE YOU WANT TO VIEW",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 16,
    :x 9,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "{:start 0,\n 0\n {:label\n  \"THE FOLLOWING FUNCTIONS CAN COMPARE NUMBERS\",\n  :states\n  [{:state 1, :label \"greater-than?\"}\n   {:state 2, :label \"less-than?\"}]},\n 1\n {:label\n  \"RETURNS true IF THE FIRST NUMBER IS BIGGER THAN THE SECOND.\\n\\n(greater-than? 5 2) RETURNS true.\\n\\n(greater-than? 2 2) RETURNS false.\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 2\n {:label\n  \"RETURNS true IF THE FIRST NUMBER IS SMALLERTHAN THE SECOND.\\n\\n(greater-than? 2 5) RETURNS true.\\n\\n(greater-than? 2 2) RETURNS false.\",\n  :states [{:state 0, :label \"Go Back\"}]},\n}\n",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Help Menu List",
    :target-ids []}}
  {:name :sound-barrier,
   :answers
   {:z 16,
    :x 10,
    :id "",
    :direction 3.1164933230697276,
    :target-ids
    ["Music Box Cuatro" "Music Box Cinco" "Music Box Seis"]}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["Stereo Cuatro"],
    :z 18,
    :x 7,
    :muted? false,
    :interactive? false,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box Cuatro"}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["Stereo Cinco"],
    :z 18,
    :x 8,
    :muted? false,
    :interactive? false,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box Cinco"}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["Stereo Seis"],
    :z 18,
    :x 9,
    :muted? false,
    :interactive? false,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box Seis"}}
  {:name :text-screen,
   :answers
   {:direction 1.570796326794896,
    :protocol :menus,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND THE NUMBER OF THE CHOICE YOU WANT TO VIEW",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 18,
    :x 19,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "{:start 0,\n 0\n {:label\n  \"THE FOLLOWING FUNCTIONS MAY BE USEFUL\",\n  :states\n  [{:state 1, :label \"pitch\"}\n   {:state 2, :label \"duration\"}\n   {:state 3, :label \"map\"}]},\n 1\n {:label\n  \"pitch\\n\\nRETURNS THE PITCH OF A NOTE.\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 2\n {:label\n  \"duration\\n\\nRETURNS THE DURATION OF A NOTE.\",\n  :states [{:state 0, :label \"Go Back\"}]}\n 3\n {:label\n  \"map\\n\\nRETURNS BACK A GIVEN LIST WITH EACH ELEMENT PROCESSED BY A FUNCTION.\\n\\n(map inc [1 2 3]) RETURNS [2 3 4]\\n\\n(map pitch [c d e]) RETURNS [60 62 64]\",\n  :states [{:state 0, :label \"Go Back\"}]}\n}\n",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Help Menu Note List",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :pass-with,
    :text
    "POGGIO INSTITUTE\n================\nSEND A FUNCTION THAT TAKES IN A LIST OF PITCHES AND RETURNS THE LIST OF NOTES WITH THOSE PITCHES.\n\nIT'D BEHOOVE YOU TO USE map.\n",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 18,
    :x 20,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "[62 62 64 62 60]",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 1-argument function fn.\nReturns: Modifies the list of notes using fn.",
    :distance 40,
    :id "Transformer Dos",
    :target-ids ["Music Box Dos"]}}
  {:name :ball-stereo,
   :answers
   {:z 19,
    :x 7,
    :id "Stereo Cuatro",
    :target-ids ["Door Cuatro-1"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["green" "green" "blue" "green" "red"]}}
  {:name :ball-stereo,
   :answers
   {:z 19,
    :x 8,
    :id "Stereo Cinco",
    :target-ids ["Door Cuatro-2"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["green" "green" "blue" "green" "red"]}}
  {:name :ball-stereo,
   :answers
   {:z 19,
    :x 9,
    :id "Stereo Seis",
    :target-ids ["Door Cuatro-3"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["green" "green" "blue" "green" "red"]}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["Stereo Dos"],
    :z 19,
    :x 21,
    :muted? false,
    :interactive? false,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box Dos"}}
  {:name :ball-stereo,
   :answers
   {:z 19,
    :x 22,
    :id "Stereo Dos",
    :target-ids ["Door Dos-1" "Door Dos-2"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["green" "green" "blue" "green" "red"]}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos [],
    :z 20,
    :x 17,
    :muted? false,
    :interactive? true,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id ""}}
  {:name :model,
   :answers
   {:z 20,
    :x 18,
    :id "",
    :model-name "Models/Penguin/Pingouin-NEW.scene",
    :direction -3.113822016996372,
    :x-delta 0.0,
    :y-delta 0.0,
    :z-delta 0.0,
    :collidable? false}}
  {:name :glass-door,
   :answers
   {:z 20,
    :x 20,
    :id "",
    :direction -0.06442683942555355,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 21,
    :x 8,
    :id "Door Cuatro-1",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 7.0}}
  {:name :sound-barrier,
   :answers
   {:z 21,
    :x 15,
    :id "",
    :direction -1.594985156453733,
    :target-ids ["Music Box Tres"]}}
  {:name :glass-door,
   :answers
   {:z 22,
    :x 8,
    :id "Door Cuatro-2",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :right,
    :speed 2.0,
    :time 7.0}}
  {:name :glass-door,
   :answers
   {:z 22,
    :x 15,
    :id "Door Tres",
    :direction -1.6142472221864275,
    :distance 8.0,
    :movement :down,
    :speed 2.0,
    :time -1.0}}
  {:name :glass-door,
   :answers
   {:z 22,
    :x 21,
    :id "Door Dos-1",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :right,
    :speed 3.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 22,
    :x 22,
    :id "Door Dos-2",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :left,
    :speed 3.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 23,
    :x 8,
    :id "Door Cuatro-3",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :down,
    :speed 2.0,
    :time 7.0}}
  {:name :sound-barrier,
   :answers
   {:z 24,
    :x 8,
    :id "",
    :direction 1.565049263631693,
    :target-ids
    ["Music Box Cuatro" "Music Box Cinco" "Music Box Seis"]}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["Stereo Tres"],
    :z 24,
    :x 15,
    :muted? false,
    :interactive? false,
    :queue-init "",
    :queue? false,
    :transformer-id "",
    :distance 24,
    :id "Music Box Tres"}}
  {:name :ball-stereo,
   :answers
   {:z 24,
    :x 16,
    :id "Stereo Tres",
    :target-ids ["Door Tres"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["blue" "green" "red" "green" "blue" "blue" "blue"]}}
  {:name :sound-barrier,
   :answers
   {:z 24,
    :x 21,
    :id "",
    :direction 1.5707963267948966,
    :target-ids ["Music Box Dos"]}}
  {:name :text-screen,
   :answers
   {:direction -0.009569085907610575,
    :protocol :pass-with,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT TAKES A LIST OF NOTES AND RETURNS THE LIST OF NOTES WITHOUT NOTES WITH THE SAME PITCH AS a.\n\nYOU CAN USE filter FOR THIS\n\n(BE WARNED THAT THE NOTES MAY HAVE DIFFERENT DURATIONS)",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 25,
    :x 15,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "[e d c (note (pitch a) 0.1) d e (note (pitch a) 1) e  e a]",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 1-argument function fn.\nReturns: Modifies the list of notes using fn.",
    :distance 40,
    :id "Transformer Tres",
    :target-ids ["Music Box Tres"]}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nGOOD JOB, LITTLE GRASSHOPPER (IS THIS AN APPROPRIATE NAME? IT'S UNCLEAR FROM YOUR RECORD).\n\nNOW YOU ARE SUPREMELY RELAXED AND HAVE ALMOST ALL OF THE TOOLS YOU NEED. \n\nPROCEED TO THE NEXT PHASE.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 26,
    :x 8,
    :success-text "",
    :font-size 0.6,
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
   {:direction 0.024839610504088883,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nfilter\n\nfilter RETURNS A GIVEN LIST ONLY WITH ELEMENTS THAT PASS A TEST.\n\n(filter even? [1 2 3 4]) RETURNS [2 4]\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 26,
    :x 15,
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
  {:name :sound-barrier,
   :answers
   {:z 26,
    :x 17,
    :id "",
    :direction 3.1314407180991073,
    :target-ids ["Music Box Tres"]}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text "EXIT",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 28,
    :x 7,
    :success-text "",
    :font-size 2.0,
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
   {:direction 1.5707963267948966,
    :protocol :open,
    :text "POGGIO INSTITUTE\n================\nSEND true TO EXIT.\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 28,
    :x 9,
    :success-text "\nGOOD JOB.",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? true,
    :success? "message",
    :error-text "Just send true!!!",
    :docstring
    "Takes: A boolean called message.\nReturns: Opens the door if message is true.",
    :distance 40,
    :id "Exit Transformer",
    :target-ids ["Exit Door"]}}
  {:name :glass-door,
   :answers
   {:z 29,
    :x 8,
    :id "Exit Door",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :up,
    :speed 5.0,
    :time 0.0}}]}
