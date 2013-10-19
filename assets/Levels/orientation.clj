{:loc [1 31],
 :dir 1.5707963267948966,
 :walls
 #{[1 32] [11 10] [12 11] [13 12] [14 13] [15 14] [0 31] [10 10]
   [12 12] [15 15] [0 32] [9 10] [11 12] [15 16] [9 11] [15 17] [16 18]
   [9 12] [11 14] [13 16] [16 19] [17 20] [9 13] [11 15] [12 16]
   [13 17] [17 21] [9 14] [11 16] [13 18] [17 22] [9 15] [13 19]
   [17 23] [9 16] [10 17] [13 20] [14 21] [16 23] [9 17] [13 21]
   [15 23] [12 21] [14 23] [13 23] [14 24] [15 25] [11 22] [13 24]
   [15 26] [11 23] [13 25] [15 27] [11 24] [15 28] [11 25] [10 25]
   [13 28] [14 29] [7 23] [8 24] [9 25] [12 28] [13 29] [5 22] [6 23]
   [8 25] [9 26] [11 28] [4 22] [5 23] [8 26] [9 27] [3 22] [8 27]
   [10 29] [2 22] [5 25] [6 26] [9 29] [2 23] [4 25] [5 26] [6 27]
   [8 29] [3 25] [6 28] [7 29] [1 23] [2 25] [0 23] [2 26] [0 24]
   [2 27] [3 28] [4 29] [0 25] [3 29] [4 30] [0 26] [4 31] [0 27]
   [0 28] [1 29] [3 32] [0 29] [2 32] [12 10] [0 30]},
 :wall-mat "Textures/paper1.jpg",
 :widgets
 [{:name :glass-door,
   :answers
   {:z 13,
    :x 11,
    :id "Exit Door",
    :direction -0.047583103276983396,
    :distance 8.0,
    :movement :up,
    :speed 4.0,
    :time 0.0}}
  {:name :text-screen,
   :answers
   {:direction -1.5750698050532461,
    :protocol :open,
    :text
    "POGGIO INSTITUTE\n================\n\nTO OPEN EXIT, SEND true.",
    :parameter "exit-code",
    :text-color {:red 127, :green 255, :blue 0},
    :z 13,
    :x 12,
    :success-text "\nGOOD BYE.",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? true,
    :success? "exit-code",
    :error-text
    "This door takes a boolean. A boolean can only be true or false. This door wants true.",
    :docstring
    "Takes: a boolean called exit-code.\nReturns: Opens exit when exit-code is true.",
    :id "Exit Console",
    :target-ids ["Exit Door"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5350972141155728,
    :protocol :none,
    :text "EXIT",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 13,
    :x 13,
    :success-text "",
    :font-size 2.0,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -1.5752605828520267,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nTHAT CONCLUDES OUR BASIC ORIENTATION.\n\nFEEL FREE TO GRAB SPECIES-APPROPRIATE REFRESHMENTS BEFORE PROCEEDING.\n\nYOU MUST USE OUR DESIGNATED EXIT FOR YOUR PROGRESS TO BE RECORDED.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 14,
    :x 14,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 17,
    :x 14,
    :id "Cannon Door 1",
    :direction -1.5707963267948966,
    :distance 8.0,
    :movement :up,
    :speed 1.0,
    :time 2.0}}
  {:name :globule-receiver,
   :answers
   {:z 20,
    :x 16,
    :id "Hoop",
    :direction -1.5582969777755349,
    :y 1,
    :target-ids ["Cannon Door 1"],
    :protocol :open-on-pattern,
    :pattern ["red" "green" "blue"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5643029115666711,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU CAN GROUP MANY VALUES TOGETHER IN LISTS.\n\nAN EMPTY LIST IS CALLED nil.\n\nLISTS CAN BE BUILT USING cons LIKE\n\n(cons 1 (cons 2 (cons 3 nil))\n\nOR YOU CAN DO IT THIS WAY:\n\n[1 2 3]",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 22,
    :x 12,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -1.566166730241185,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nOR YOU CAN USE FUNCTIONS IN THE List MODULE.\n\n\nEXAMPLE:\n(repeat 3)  \n-- RETURNS AN INFINITE LIST OF 3.\n\n(concat [1 2 3] [4 5 6]) \n-- ADDS THE LISTS TOGETHER TO MAKE [1 2 3 4 5 6]\n\n(flatten [ [1 2 3] [4 5 6] ]\n-- ADDS THE LISTS INSIDE THE LIST TO MAKE [1 2 3 4 5 6]\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 22,
    :x 13,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
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
    :z 22,
    :velocity 50,
    :x 16,
    :transform-id "",
    :mass 0.5,
    :queue? false,
    :id "Cannon 1"}}
  {:name :text-screen,
   :answers
   {:direction -3.141592653589793,
    :protocol :open,
    :text
    "POGGIO INSTITUTE\n================\n\nRIGHT CLICK OBJECTS TO GET MORE INFORMATION ON THEM.\n\nLEFT CLICK TO SEND FUNCTIONS/MESSAGES.\n\nSEND THE NUMBER 3 TO THIS CONSOLE TO OPEN THE DOOR.\n\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 23,
    :x 4,
    :success-text
    "GOOD JOB. YOU'RE ALREADY A CUT ABOVE THE AVERAGE MONKEY WE HIRE.",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "(equal? message 3)",
    :error-text "Send the number 3 to open the door.",
    :docstring
    "Takes: a number, n.\nReturns: Opens the door when n = 3.",
    :id "Door Console 1",
    :target-ids ["Test Door"]}}
  {:name :glass-door,
   :answers
   {:z 24,
    :x 5,
    :id "Test Door",
    :direction -3.107123552590285,
    :distance 8.0,
    :movement :up,
    :speed 3.0,
    :time 0.0}}
  {:name :text-screen,
   :answers
   {:direction -3.141592653589793,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU CAN USE FUNCTIONS FROM THE NUMBER MODULE TO DO OTHER NEAT TRICKS WITH NUMBERS.\n\nWHENEVER YOU USE A FUNCTION, YOU ENCLOSE IT IN PARENTHESES.\n\nEXAMPLES:\n\n(add 1 3)\n\n(multiply 3.5 (add 1 1))\n\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 24,
    :x 7,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -0.030447440569171202,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nALL EMPLOYEES ARE ADVISED NOT TO DO THE FOLLOWING:\n\n1. DO NOT ABUSE/HACK PROPERTY OF POGGIO INSTITUTE.\n\n2. DO NOT BRING ANIMALS, LIVING OR DEAD, ONTO POGGIO INSTITUTE PROPERTY.\n\n3. DO NOT TREAT TUESDAY AS CASUAL FRIDAY.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 25,
    :x 1,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :open,
    :text
    "POGGIO INSTITUTE\n================\n\nPRACTICE USING FUNCTIONS HERE.\n\nSEND TO THIS CONSOLE 555 TIMES 7.\n\n\"IF YOU CHEAT. YOU'RE ONLY CHEATING YOURSELF.\"\n\n~ YOUR MOTHER AT SOME POINT, PROBABLY.",
    :parameter "product",
    :text-color {:red 127, :green 255, :blue 0},
    :z 25,
    :x 6,
    :success-text
    "\nGOOD JOB. BELIEVE IT OR NOT, WE'VE HAD SUBJECTS STARVE AT THIS JUNCTURE.",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "(equal? product (multiply 555 7))",
    :error-text
    "Send 555 times 7 to access the door. Have you tried using (multiply _ _)?",
    :docstring
    "Takes: a number called n.\nReturns: Opens Test Door 2 when n = 500 times 7.",
    :id "Door Console 2",
    :target-ids ["Test Door 2"]}}
  {:name :globule-receiver,
   :answers
   {:z 25,
    :x 14,
    :id "",
    :direction -1.565845872186053,
    :y 1,
    :target-ids [],
    :protocol :open-on-pattern,
    :pattern []}}
  {:name :glass-door,
   :answers
   {:z 26,
    :x 7,
    :id "Test Door 2",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :up,
    :speed 3.0,
    :time 0.0}}
  {:name :color-screen,
   :answers
   {:z 26, :x 10, :id "Color Screen", :direction -1.544312454197166}}
  {:name :text-screen,
   :answers
   {:direction -1.5892444181860532,
    :protocol :pass,
    :text
    "POGGIO INSTITUTE\n================\n\nYOU CAN SEND THINGS THAT AREN'T NUMBERS LIKE COLORS.\n\nTRY IT OUT HERE.\n\nYOU CAN SEND red, green, OR blue.\n\nOR YOU COULD MAKE YOUR OWN COLOR WITH color.\n\ncolor TAKES A RED, GREEN, AND BLUE VALUE BETWEEN 0-255.\n\nEXAMPLE:\n(color 100 0 150)",
    :parameter "c",
    :text-color {:red 127, :green 255, :blue 0},
    :z 26,
    :x 11,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: a color called c.\nReturns: Changes Color Screen to c.",
    :id "Color Console",
    :target-ids ["Color Screen"]}}
  {:name :glass-door,
   :answers
   {:z 26,
    :x 13,
    :id "",
    :direction -3.141592653589793,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 27,
    :x 13,
    :id "",
    :direction -3.109345771154539,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :text-screen,
   :answers
   {:direction -1.553943989753554,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nCLICK AND DRAG ON THE SCREEN TO MOVE THE CAMERA.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 28,
    :x 2,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nHEY! SPENCER! CHECK THIS OUT! I FIGURED OUT HOW TO HACK THESE PUPPIES!",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 28,
    :x 7,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nHA! GOOD FIND, IAN!\n\nHEY. \nHEY.\n\n\nHEY.\n\nCHECK THIS OUT!\n\n<--------\n<--------",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 28,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :none,
    :text "MEOW!\n ^   ^\n(=^.^=)",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 28,
    :x 9,
    :success-text "",
    :font-size 1.8,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "(repeat (color 49 0 98))",
    :constraint "",
    :z 28,
    :velocity 75,
    :x 14,
    :transform-id "",
    :mass 0.5,
    :queue? true,
    :id "blocked cannon 1"}}
  {:name :text-screen,
   :answers
   {:direction -1.5769690878996776,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\n\n\nUSE WASD KEYS TO MOVE.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 30,
    :x 1,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction -1.5551725981744198,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\n\nPROCEED TO ORIENTATION.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 30,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :transform "(function x x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :id "",
    :target-ids []}}]}
