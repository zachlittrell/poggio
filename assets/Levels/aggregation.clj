{:loc [1 31],
 :dir 1.5707963267948966,
 :walls
 #{[1 32] [26 25] [29 28] [0 31] [25 25] [26 26] [28 28] [0 32] [24 25]
   [25 26] [27 28] [23 25] [26 28] [22 25] [25 28] [21 25] [21 26]
   [24 29] [20 26] [21 27] [19 26] [23 30] [18 26] [21 29] [22 30]
   [17 26] [19 28] [21 30] [13 23] [17 27] [19 29] [20 30] [12 23]
   [13 24] [15 26] [17 28] [19 30] [11 23] [13 25] [14 26] [15 27]
   [16 28] [18 30] [10 23] [13 26] [15 28] [17 30] [8 22] [9 23]
   [11 25] [16 30] [7 22] [10 25] [11 26] [16 31] [6 22] [9 25] [10 26]
   [15 31] [5 22] [10 27] [11 28] [15 32] [4 22] [5 23] [8 26] [10 28]
   [14 32] [3 22] [7 26] [10 29] [13 32] [3 23] [6 26] [7 27] [10 30]
   [12 32] [2 23] [5 26] [7 28] [10 31] [11 32] [3 25] [4 26] [7 29]
   [3 26] [29 20] [30 21] [31 22] [1 24] [6 30] [28 20] [31 23] [1 25]
   [5 30] [27 20] [31 24] [1 26] [3 29] [4 30] [28 22] [29 23] [31 25]
   [1 27] [4 31] [26 21] [27 22] [28 23] [30 25] [1 28] [29 25] [1 29]
   [3 32] [27 24] [29 26] [2 32] [29 27] [0 30]},
 :wall-mat "Textures/tile1.png",
 :widgets
 [{:name :text-screen,
   :answers
   {:direction 3.1318368655442788,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT SUMS UP A GIVEN LIST,\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 23,
    :x 4,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] \n   5)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "Sum Transformer",
    :target-ids []}}
  {:name :globule-receiver,
   :answers
   {:z 23,
    :x 6,
    :id "15-Hoop",
    :direction -1.5537525227998992,
    :y 1,
    :target-ids ["Door 1"],
    :protocol :open-on-pattern,
    :pattern ["15"]}}
  {:name :globule-receiver,
   :answers
   {:z 23,
    :x 7,
    :id "0-Hoop-1",
    :direction -1.5856467201739763,
    :y 1,
    :target-ids ["Door 2"],
    :protocol :open-on-pattern,
    :pattern ["0"]}}
  {:name :globule-receiver,
   :answers
   {:z 23,
    :x 8,
    :id "0-Cannon-2",
    :direction -1.5462111174250366,
    :y 1,
    :target-ids ["Door 3"],
    :protocol :open-on-pattern,
    :pattern ["0"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5960434863390587,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU CAN PICK APART A LIST USING head AND tail.\n\nEXAMPLES:\n\n(head [1 2 3 4]) \n-- RETURNS 1\n\n(tail [1 2 3 4])\n-- RETURNS [2 3 4]\n\nTO PROCESS A LIST, YOU CAN USE reduce OR BUILD A NEW FUNCTION.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 24,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 24,
    :x 9,
    :id "Door 1",
    :direction -3.0860941483440762,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 24,
    :x 10,
    :id "Door 2",
    :direction -3.141592653589793,
    :distance 8.0,
    :movement :right,
    :speed 4.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 24,
    :x 11,
    :id "Door 3",
    :direction 3.0890095919788516,
    :distance 16.0,
    :movement :down,
    :speed 6.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 24,
    :x 29,
    :id "Exit Door",
    :direction 3.118869292748152,
    :distance 8.0,
    :movement :up,
    :speed 5.0,
    :time 0.0}}
  {:name :text-screen,
   :answers
   {:direction 0.01004991289303437,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nHEY!\n\nI CAN'T HEAR MY JAM ANYMORE!\n\nBOOOOOOOOOOOOOOOOOOOO!\n\nTHOU SHALT FEEL MY WRATH OVER THIS, BOSSMAN!",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 25,
    :x 2,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :function-cannon,
   :answers
   {:direction 1.5374753309166493,
    :init-queue "(repeat [1 2 3 4 5])",
    :constraint "",
    :z 25,
    :velocity 75,
    :x 6,
    :transform-id "Sum Transformer",
    :mass 0.5,
    :queue? true,
    :id "1-2-3-4-5-Cannon"}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat [-5 3 5 -3])",
    :constraint "",
    :z 25,
    :velocity 75,
    :x 7,
    :transform-id "Sum Transformer",
    :mass 0.5,
    :queue? true,
    :id "-5-3--5--3-Cannon"}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat nil)",
    :constraint "",
    :z 25,
    :velocity 75,
    :x 8,
    :transform-id "Sum Transformer",
    :mass 0.5,
    :queue? true,
    :id "Nil-Cannon"}}
  {:name :text-screen,
   :answers
   {:direction 3.0903554861863745,
    :protocol :open,
    :text "POGGIO INSTITUTE\n================\n\nTO EXIT, SEND true.",
    :parameter "exit-code",
    :text-color {:red 127, :green 255, :blue 0},
    :z 25,
    :x 28,
    :success-text "GOOD BYE.",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? true,
    :success? "exit-code",
    :error-text "",
    :docstring
    "Takes: A boolean called exit-code.\nReturns: Opens the exit if exit-code is true.",
    :id "Exit Console",
    :target-ids ["Exit Door"]}}
  {:name :sound-barrier,
   :answers
   {:z 26,
    :x 2,
    :id "",
    :direction -1.6156093540546363,
    :target-ids "hmb1"}}
  {:name :sound-barrier,
   :answers
   {:z 26,
    :x 12,
    :id "",
    :direction -1.5548614206888203,
    :target-ids "Restful Music Box"}}
  {:name :globule-receiver,
   :answers
   {:z 26,
    :x 22,
    :id "Range Hoop",
    :direction -1.605993053181395,
    :y 1,
    :target-ids ["Octave Music Box"],
    :protocol :pass,
    :pattern []}}
  {:name :music-box,
   :answers
   {:z 26,
    :x 23,
    :id "Octave Music Box",
    :direction -2.378413055519064,
    :stereos ["Octave Stereo"],
    :transformer-id "Cannon To Note Transform",
    :queue? true,
    :queue-init "",
    :interactive? false}}
  {:name :ball-stereo,
   :answers
   {:z 26,
    :x 24,
    :id "Octave Stereo",
    :target-ids ["Octave Door"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern
    ["color 255 0 0"
     "color 0 255 0"
     "color 0 0 255"
     "color 255 255 0"
     "color 0 255 255"
     "color 255 0 255"
     "color 255 255 255"
     "color 255 0 0"]}}
  {:name :text-screen,
   :answers
   {:direction -3.1324604873703823,
    :protocol :none,
    :text "EXIT",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 26,
    :x 28,
    :success-text "",
    :font-size 2.0,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 27,
    :x 3,
    :id "",
    :direction -3.124050593532391,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :ball-stereo,
   :answers
   {:z 27,
    :x 5,
    :id "hs1",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id ""}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :pass-with,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT REMOVES THE NOTE a FROM A LIST OF NOTES.\n\nRECALL NOTES ARE IN THIS ORDER;\n\nC-C#-D-D#-E-F-F#-G-G#-A-A#-B\n\nWE SAY C'S PITCH IS 60.\n\nYOU MAY FIND filter HANDY FOR THIS.",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 27,
    :x 11,
    :success-text "",
    :font-size 0.6,
    :transform "(flatten (repeat [d d a a a e d c a]))",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 1-parameter function called fn.\nReturns: Processes the list of notes using fn.",
    :id "No Rest Console",
    :target-ids ["Restful Music Box"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5444866095419745,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU HEAR THAT, SPENCER?\n\nI BELIEVE BOSSMAN WANTS MORE BALLPITS AND TO HEAR OUR JAMS ALL OVER THE PLACE.",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 27,
    :x 18,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -3.1228155179973607,
    :protocol :none,
    :text "}:-]",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 27,
    :x 20,
    :success-text "",
    :font-size 2.0,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 27,
    :x 25,
    :id "Octave Door",
    :direction 3.1172072444170746,
    :distance 8.0,
    :movement :up,
    :speed 2.0,
    :time 0.0}}
  {:name :sound-barrier,
   :answers
   {:z 27,
    :x 26,
    :id "",
    :direction 3.0933844201587752,
    :target-ids "Octave Music Box"}}
  {:name :text-screen,
   :answers
   {:direction -3.1088175091857173,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nWE HOPE YOU FOUND THIS MINOR INDULGENCE IN MUSIC HAS BOTH PREPARED YOU FOR OUR LITTLE SURPRISE, AND ALSO MADE YOU LESS LIKELY TO HAVE A PANIC ATTACK WHEN WE SHOW YOU OUR LITTLE SURPRISE.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 27,
    :x 28,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 28,
    :x 3,
    :id "",
    :direction -3.113822016996372,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :music-box,
   :answers
   {:direction 1.5707963267948966,
    :stereos ["hs1" "hs2" "hs3"],
    :z 28,
    :x 5,
    :interactive? false,
    :queue-init
    "(flatten (repeat \n[(rest 1)\n  e d c d e e e\n  (rest 0.25)\n   d d d \n   (rest 0.25)\n   e g g\n   (rest 0.25)\n   e d c d e e e e d d e d c]))",
    :queue? true,
    :transformer-id "",
    :id "hmb1"}}
  {:name :ball-stereo,
   :answers
   {:z 28,
    :x 6,
    :id "hs2",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id ""}}
  {:name :music-box,
   :answers
   {:z 28,
    :x 12,
    :id "Restful Music Box",
    :direction 1.5707963267948966,
    :stereos ["Restful Stereo"],
    :transformer-id "",
    :queue? false,
    :queue-init "",
    :interactive? false}}
  {:name :sound-barrier,
   :answers
   {:z 28,
    :x 21,
    :id "",
    :direction -0.006493415228225435,
    :target-id "Octave Music Box"}}
  {:name :text-screen,
   :answers
   {:direction -3.1218907696769844,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION TO CONVERT GLOBULES FROM THE CANNON INTO NOTES.",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 28,
    :x 24,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x]\n  c)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: a unary function called fn.\nReturns: Converts values with fn.",
    :id "Cannon To Note Transform",
    :target-ids []}}
  {:name :ball-stereo,
   :answers
   {:z 29,
    :x 5,
    :id "hs3",
    :target-ids [],
    :protocol :open-on-pattern,
    :transformer-id ""}}
  {:name :glass-door,
   :answers
   {:z 29,
    :x 16,
    :id "Restless Door",
    :direction -3.123412838516815,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 3.0}}
  {:name :sound-barrier,
   :answers
   {:z 29,
    :x 17,
    :id "",
    :direction -3.134926085685925,
    :target-ids "Restful Music Box"}}
  {:name :text-screen,
   :answers
   {:direction -3.1206533060790846,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nSOUND BARRIERS WERE RECENTLY INSTALLED, AS A REMINDER TO EMPLOYEES OF POGGIO INSTITUTE THAT MUSIC BOXES ARE A PRIVELEGE, NOT A RIGHT TO ANNOY MAINTENANCE WORKERS.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 29,
    :x 18,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "",
    :constraint "",
    :z 29,
    :velocity 75,
    :x 22,
    :transform-id "",
    :mass 0.5,
    :queue? false,
    :id "Range Cannon"}}
  {:name :text-screen,
   :answers
   {:direction -1.5837266406100736,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nIN THESE HALLS WE HAVE BEEN WORKING TO RECREATE SOMETHINGS FAMILIAR TO YOU.\n\nI HOPE OUR EFFORTS ARE APPRECIATED.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 30,
    :x 1,
    :success-text "",
    :font-size 0.6,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :ball-stereo,
   :answers
   {:z 30,
    :x 13,
    :id "Restful Stereo",
    :target-ids ["Restless Door"],
    :protocol :open-on-pattern,
    :transformer-id "",
    :pattern ["green" "green" "blue" "green" "red"]}}]}
