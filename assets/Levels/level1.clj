(do
 (do
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.glass-door)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.function-cannon)
  (clojure.core/use 'tools.level-editor.widgets.globule-receiver)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.color-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen)
  (clojure.core/use 'tools.level-editor.widgets.text-screen))
 {:loc [1 31],
  :dir 1.5707963267948966,
  :walls
  #{[1 32] [20 19] [25 24] [0 31] [20 20] [21 21] [24 24] [20 21]
    [23 24] [19 21] [21 23] [22 24] [23 25] [18 21] [21 24] [23 26]
    [17 21] [20 24] [23 27] [16 21] [23 28] [20 26] [23 29] [15 22]
    [19 26] [22 29] [15 23] [18 26] [21 29] [15 24] [17 26] [19 28]
    [20 29] [15 25] [16 26] [19 29] [15 26] [19 30] [14 26] [18 30]
    [13 26] [17 30] [12 26] [16 30] [11 26] [13 28] [15 30] [7 23]
    [10 26] [13 29] [14 30] [6 23] [7 24] [9 26] [13 30] [5 23] [7 25]
    [8 26] [12 30] [4 23] [7 26] [11 30] [3 23] [4 24] [7 27] [9 29]
    [10 30] [25 13] [26 14] [2 23] [7 28] [10 31] [24 13] [4 26] [6 28]
    [7 29] [23 13] [25 15] [1 23] [6 29] [9 32] [22 13] [24 15] [25 16]
    [1 24] [2 26] [3 27] [8 32] [21 13] [23 15] [25 17] [2 27] [3 28]
    [4 29] [7 32] [21 14] [25 18] [26 19] [0 25] [3 29] [4 30] [6 32]
    [21 15] [25 19] [26 20] [0 26] [4 31] [5 32] [20 15] [22 17]
    [24 19] [26 21] [0 27] [4 32] [20 16] [21 17] [23 19] [26 22]
    [0 28] [1 29] [3 32] [22 19] [26 23] [0 29] [2 32] [20 18] [0 30]},
  :wall-mat "Textures/paper1.jpg",
  :widgets
  [(do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction 1.5707963267948966,
       :movement :up,
       :speed 5.0,
       :z 15,
       :x 22,
       :app app__7296__auto__,
       :time 0.0,
       :distance 8.0,
       :id "door6"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5318549836978574,
       :protocol :open,
       :text
       "POGGIO INSTITUTE\n================\n\nTO UNLOCK THE EXIT, SEND true.\n",
       :parameter "message",
       :z 16,
       :x 21,
       :success-text "\nGOOD BYE.",
       :font-size 0.6,
       :end-level? true,
       :target-id "door6",
       :success? "message",
       :error-text "You must send true to unlock the door.",
       :app app__1572__auto__,
       :docstring
       "Takes: a boolean b.\nDoes: Unlocks the door if b is true.",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5825604899446555,
       :protocol :none,
       :text "EXIT",
       :parameter "message",
       :z 16,
       :x 24,
       :success-text "",
       :font-size 2.0,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction 1.5707963267948966,
       :movement :left,
       :speed 1.0,
       :z 19,
       :x 21,
       :app app__7296__auto__,
       :time 2.0,
       :distance 8.0,
       :id "door5"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5841288700890424,
       :protocol :hold,
       :text
       "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION TO THIS CONSOLE\nTHAT NUMBERS TO COLORS.",
       :parameter "message",
       :z 20,
       :x 22,
       :transform-param "x"
       :transform "x"
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id "usertransform"})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction -1.5841288700890424,
       :z 20,
       :velocity 100,
       :x 23,
       :transform-id "",
       :constraint "number"
       :app app__866__auto__,
       :mass 0.5,
       :queue? false,
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.115957245068116,
       :movement :up,
       :speed 0.0,
       :z 20,
       :x 24,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction -1.5634435181282058,
       :protocol :open,
       :pattern ["red" "green" "blue"],
       :z 20,
       :y 1,
       :x 25,
       :target-id "door5",
       :app app__960__auto__,
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :hold,
       :text "POGGIO INSTITUTE\n================\n",
       :parameter "message",
       :z 21,
       :x 15,
       :success-text "",
       :transform-param "x"
       :transform "if (equal? x 1) red (if (equal? x 2) green (if (equal? x 3) blue (color 0 0 0)))"
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id "transformer1"})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.141592653589793,
       :movement :up,
       :speed 0.0,
       :z 21,
       :x 24,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction -1.5544934234762344,
       :protocol :open,
       :pattern ["red" "green" "blue"],
       :z 22,
       :y 1,
       :x 16,
       :target-id "door4",
       :app app__960__auto__,
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -0.02702044918726479,
       :movement :up,
       :speed 0.0,
       :z 22,
       :x 17,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction -1.5920697128189578,
       :z 22,
       :velocity 100,
       :x 18,
       :transform-id "",
       :constraint "number"
       :app app__866__auto__,
       :mass 0.5,
       :queue? false,
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.6345396393275837,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nTHESE CANNONS CONVERT\n1 TO RED\n2 TO GREEN\n3 TO BLUE.\n",
       :parameter "message",
       :z 22,
       :x 19,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.141592653589793,
       :movement :left,
       :speed 1.0,
       :z 22,
       :x 21,
       :app app__7296__auto__,
       :time 2.0,
       :distance 8.0,
       :id "door4"})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.1031500635686053,
       :movement :up,
       :speed 0.0,
       :z 22,
       :x 24,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -0.047583103276983396,
       :movement :up,
       :speed 0.0,
       :z 23,
       :x 17,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nYOU CAN CREATE YOUR OWN FUNCTIONS.\n\nTAKE A LOOK AT OTHER FUNCTIONS TO GET\nAN IDEA OF HOW TO WRITE YOUR OWN.",
       :parameter "message",
       :z 23,
       :x 22,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction 1.5707963267948966,
       :protocol :pass,
       :pattern [],
       :z 23,
       :y 1,
       :x 23,
       :target-id "hiddencannon2",
       :app app__960__auto__,
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.141592653589793,
       :movement :up,
       :speed 0.0,
       :z 23,
       :x 24,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction 1.5707963267948966,
       :z 23,
       :velocity 100,
       :x 25,
       :transform-id "usertransform",
       :app app__866__auto__,
       :mass 0.5,
       :queue? true,
       :id "hiddencannon2"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.1136669541971806,
       :protocol :open,
       :text
       "POGGIO INSTITUTE\n================\n\nYOU CAN SEND VALUES TO CERTAIN OBJECTS.\n\nRIGHT CLICK AN OBJECT TO GET MORE INFO.\n\nLEFT CLICK AN OBJECT TO SEND A VALUE.\n\nTO UNLOCK THE DOOR, SEND THE NUMBER 3\nTO THIS CONSOLE SCREEN.",
       :parameter "message",
       :z 24,
       :x 3,
       :success-text
       "\nGOOD JOB.\nIT'S GOOD TO SEE YOU AREN'T LIKE\nOUR REGULAR MONKEYS.",
       :font-size 0.6,
       :end-level? false,
       :target-id "door1",
       :success? "(equal? 3 message)",
       :error-text
       "To unlock the door, you need to send the number 3.",
       :app app__1572__auto__,
       :docstring
       "Takes: a number n.\nDoes: Opens the door if n is 3.",
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -0.0570807824062646,
       :movement :up,
       :speed 0.0,
       :z 24,
       :x 17,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.113822016996372,
       :movement :up,
       :speed 3.0,
       :z 25,
       :x 4,
       :app app__7296__auto__,
       :time 0.0,
       :distance 8.0,
       :id "door1"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.121187322903255,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nYOU CAN SEND MORE COMPLICATED MESSAGES LIKE:\n\nadd 1 1\n\nmultiply 2 (add 1 1)\n\nequal? 2 2\n\nless-than? 1 2",
       :parameter "message",
       :z 25,
       :x 6,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction 1.5707963267948966,
       :z 25,
       :velocity 100,
       :x 16,
       :transform-id "transformer1",
       :app app__866__auto__,
       :mass 0.5,
       :queue? true,
       :id "hiddencannon1"})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -0.03224688243525392,
       :movement :up,
       :speed 0.0,
       :z 25,
       :x 17,
       :app app__7296__auto__,
       :time 0.0,
       :distance 0.0,
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction 1.5707963267948966,
       :protocol :pass,
       :pattern "",
       :z 25,
       :y 1,
       :x 18,
       :target-id "hiddencannon1",
       :app app__960__auto__,
       :id nil})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.125720970598003,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\n ....CRAP...GOOD GAME...",
       :parameter "message",
       :z 25,
       :x 22,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -0.018179815072978278,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nREMINDER TO ALL EMPLOYEES:\n\nTHE FOLLOWING SHOULD NOT HAPPEN:\n\n1. HACKING POGGIO INSTITUTE PROPERTY.\n2. LEAVING DEAD ANIMALS ON THE PREMISES.\n3. TREATING TUESDAY AS CASUAL FRIDAY.",
       :parameter "message",
       :z 26,
       :x 1,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.0140830953745548,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\n | |0 \n0|x| \nx|x|\n",
       :parameter "message",
       :z 26,
       :x 22,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -0.05449145624073119,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nHAA! COOL!\n\nCHECK THIS OUT!\n\n<=====\n",
       :parameter "message",
       :z 27,
       :x 4,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.0993642514767914,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nAWWWWHH MAN.\n\nHEY SAMMY! I FIGURED OUT HOW TO HACK INTO THESE PUPPIES!",
       :parameter "message",
       :z 27,
       :x 6,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.6155425582495124,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nYOU CAN GROUP VALUES TOGETHER IN LISTS.\n\nSOME HANDY LIST FUNCTIONS:\n\nnil - RETURNS AN EMPTY LIST\ncons head tail - RETURNS A LIST WITH head AT THE FRONT, FOLLOWED BY tail.\nEX. cons 1 nil\nrepeat x - RETURNS AN INFINITE LIST OF x.\ntriple a b c - RETURNS A LIST OF a, b, c.\n",
       :parameter "message",
       :z 27,
       :x 8,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction 3.108271657711546,
       :movement :left,
       :speed 3.0,
       :z 27,
       :x 13,
       :app app__7296__auto__,
       :time 0.0,
       :distance 8.0,
       :id "door2"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5901487492103514,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nBY COMBINING BASIC FUNCTIONS LIKE REPEAT,\nYOU CAN MAKE MORE POWERFUL FUNCTIONS.",
       :parameter "message",
       :z 27,
       :x 16,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__7296__auto__]
     (tools.level-editor.widgets.glass-door/build-glass-door
      {:direction -3.0828368308740703,
       :movement :left,
       :speed 1.0,
       :z 27,
       :x 19,
       :app app__7296__auto__,
       :time 2.0,
       :distance 8.0,
       :id "door3"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -3.121187322903255,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\n | |0 \n0|x| \nx| |\n",
       :parameter "message",
       :z 27,
       :x 22,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.4478244572305994,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nCLICK AND DRAG ON THE SCREEN\nTO MOVE YOUR CAMERA.",
       :parameter "message",
       :z 28,
       :x 2,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -0.13640260440094745,
       :protocol :none,
       :text "MEOW!",
       :parameter "message",
       :z 28,
       :x 4,
       :success-text "",
       :font-size 3.0,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction -0.0,
       :z 28,
       :velocity 75,
       :x 9,
       :transform-id "",
       :app app__866__auto__,
       :mass 0.5,
       :queue? false,
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction -3.1314407180991073,
       :protocol :open,
       :pattern ["red" "green" "blue"],
       :z 28,
       :y 1,
       :x 12,
       :target-id "door2",
       :app app__960__auto__,
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nHEY SAMMY! TIC TAC TOE!\n\n | | \n |x| \n | |\n",
       :parameter "message",
       :z 28,
       :x 20,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\n | | \n0|x| \n | |\n",
       :parameter "message",
       :z 28,
       :x 21,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\n | | \n0|x| \nx| |\n",
       :parameter "message",
       :z 28,
       :x 22,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__866__auto__]
     (tools.level-editor.widgets.function-cannon/build-function-cannon
      {:direction -0.0,
       :z 29,
       :velocity 100,
       :x 14,
       :transform-id "",
       :app app__866__auto__,
       :mass 0.5,
       :queue? false,
       :id ""})))
   (do
    (clojure.core/fn
     [app__960__auto__]
     (tools.level-editor.widgets.globule-receiver/build-globule-receiver
      {:direction -3.1305433795104243,
       :protocol :open,
       :pattern ["red" "green" "blue"],
       :z 29,
       :y 1,
       :x 18,
       :target-id "door3",
       :app app__960__auto__,
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5904016576521014,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nWELCOME NEW EMPLOYEE.\n\nUSE WASD KEYS TO MOVE AROUND.\n",
       :parameter "message",
       :z 30,
       :x 1,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction -1.5817848954776303,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nPROCEED TO ORIENTATION.\n",
       :parameter "message",
       :z 30,
       :x 3,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1008__auto__]
     (tools.level-editor.widgets.color-screen/build-color-screen
      {:direction 3.1290933045704317,
       :z 30,
       :x 9,
       :app app__1008__auto__,
       :id "colorscreen1"})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 1.5707963267948966,
       :protocol :none,
       :text
       "POGGIO INSTITUTE\n================\n\nEMPLOYEES SHOULD FEEL FREE TO PERUSE\nTHE MODULES TO SEE WHAT DIFFERENT FUNCTIONS AND VALUES THEY CAN USE.\n",
       :parameter "message",
       :z 31,
       :x 5,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring "",
       :id ""})))
   (do
    (clojure.core/fn
     [app__1572__auto__]
     (tools.level-editor.widgets.text-screen/build-text-screen
      {:direction 3.127100914524293,
       :protocol :pass,
       :text
       "POGGIO INSTITUTE\n================\n\nYOU CAN SEND VALUES BESIDES NUMBERS, LIKE COLORS.\n\nGO AHEAD AND SEND A COLOR TO THIS CONSOLE.\n",
       :parameter "message",
       :z 31,
       :x 9,
       :success-text "",
       :font-size 0.6,
       :end-level? false,
       :target-id "colorscreen1",
       :success? "",
       :error-text "",
       :app app__1572__auto__,
       :docstring
       "Takes: a color c.\nDoes: Changes the color of the nearby wall to c.",
       :id ""})))]})
