{:loc [2 1],
 :dir -0.7631795980707292,
 :soundtrack ["Music/16 - Japan Tsunami Fundraiser - ambient_samurai-Senses_Planned_For_Oblivion.ogg"]
 :walls
 #{[4 3] [6 5] [8 7] [12 11] [14 13] [17 16] [1 0] [5 5] [9 9] [14 14]
   [16 16] [1 1] [4 5] [6 7] [8 9] [10 11] [11 12] [13 14] [15 16]
   [1 2] [3 5] [6 8] [7 9] [9 11] [11 13] [12 14] [15 17] [16 18] [1 3]
   [2 5] [6 9] [8 11] [11 14] [13 16] [16 19] [1 4] [2 6] [6 10] [7 11]
   [12 16] [13 17] [1 5] [2 7] [6 11] [11 16] [13 18] [14 19] [15 20]
   [2 8] [11 17] [2 9] [5 12] [11 18] [2 10] [5 13] [2 11] [3 12]
   [5 14] [7 16] [10 19] [2 12] [3 13] [9 19] [3 14] [5 16] [8 19]
   [3 15] [4 16] [5 17] [7 19] [5 18] [6 19] [12 0] [11 0] [12 1]
   [10 0] [12 2] [9 0] [10 1] [12 3] [8 0] [12 4] [7 0] [12 5] [15 8]
   [16 9] [17 10] [6 0] [11 5] [17 11] [18 12] [5 0] [10 5] [14 9]
   [18 13] [10 6] [15 11] [18 14] [3 0] [4 1] [6 3] [8 5] [10 7]
   [13 10] [4 2] [6 4] [7 5] [8 6] [10 8] [14 12] [17 15] [2 0]},
 :wall-mat "Textures/lumber.png",
 :widgets
 [{:name :text-screen,
   :answers
   {:direction -1.5646235656901157,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nRECALL HOW CELL WALLS WORK:\n\nEACH CELL cur IS DETERMINED BY A RULE AND ITS NEIGHBORS.\n\n[left] [old] [right]\n    [cur]\n\ncur EQUALS (rule left old right)\n\nIF cur IS AT THE EDGE AND HAS NO left, WE SET left TO false.\n\nSAME GOES FOR right.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 1,
    :x 5,
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
   {:direction -1.6116826194050422,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nNOW MY DEAR MONKEY, YOU SHALL CONSTRUCT HOW CELL WALLS WORK.\n\n\"I BELIEVE IN YOU.\"\n~ YOUR PARENTS",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 1,
    :x 6,
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
   {:direction -1.5060720453793046,
    :protocol :menus,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND WHICH OPTION YOU WOULD LIKE TO VIEW",
    :parameter "choice",
    :text-color {:red 127, :green 255, :blue 0},
    :z 1,
    :x 8,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "{:start 0,\n 0\n {:label \"THE FOLLOWING FUNCTIONS MAY BE HELPFUL\",\n  :states\n  [{:state 1, :label \"concat\"}\n   {:state 2, :label \"take\"}\n   {:state 3, :label \"drop\"}\n   {:state 4, :label \"get\"}\n   {:state 5, :label \"zip\"}]},\n 1\n {:label\n  \"concat\\n\\nconcat CAN SMOOSH TWO LISTS TOGETHER.\\n\\n(concat [1 2 3] [4 5 6])\\nRETURNS [1 2 3 4 5 6]\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 2\n {:label\n  \"take\\n\\ntake TAKES THE FIRST n ITEMS.\\n\\n(take 3 [1 2 3 4])\\nRETURNS [1 2 3]\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 3\n {:label\n  \"drop\\n\\ndrop DROPS THE FIRST n ITEMS.\\n\\n(drop 3 [1 2 3 4])\\nRETURNS [4]\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 4\n {:label\n  \"get\\n\\nget RETURNS THE iTH ELEMENT OF A LIST (STARTING AT 0)\\n\\n(get 0 [red green blue]) RETURNS red\\n\\n(get 2 [red green blue]) RETURNS BLUE\",\n  :states [{:state 0, :label \"Go Back\"}]},\n 5\n {:label\n  \"zip\\n\\nzip ZIPS TOGETHER TWO LISTS (zip3 WORKS WITH 3 LISTS)\\n\\n(zip [1 2] [3 4]) RETURNS [[1 3] [2 4]]\\n\\n(zip [1] [red green blue]) RETURNS [[1 red]])\\n\\n(zip3 [1 2] [4 5] [7 8]) RETURNS [1 4 7] [1 2 8]\",\n  :states [{:state 0, :label \"Go Back\"}]}}\n",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Help Screen Eins",
    :target-ids []}}
  {:name :text-screen,
   :answers
   {:direction 3.1325431268760098,
    :protocol :pass,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT TAKES IN A RULE AND  ROW OF CELLS AND COMPUTES THE NEXT GENERATION.\n\nEXAMPLE:\n\n(rower \n  (function [x y z]\n     (not y))\n   [false false true false false])\n\nRETURNS\n[true true false true true]\n\n\n\n",
    :parameter "rower",
    :text-color {:red 127, :green 255, :blue 0},
    :z 1,
    :x 9,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 2-argument function called rower.\nReturns: Builds the cell walls using the rower.",
    :distance 40,
    :id "Transformer Eins",
    :target-ids ["Cell Wall Eins" "Cell Wall Zwei"]}}
  {:name :text-screen,
   :answers
   {:direction 3.122456233886442,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nHEY, IT'S THE BIG OLE GREEN PLACE AGAIN.\n\nSTILL NO BATHROOMS, THOUGH.",
    :parameter "message",
    :text-color {:red 51, :green 153, :blue 255},
    :z 3,
    :x 3,
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
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :all-transform,
    :z 4,
    :init-id "",
    :x 7,
    :precompute? true,
    :rule-id "",
    :target-id "",
    :row-id "",
    :pair-id "",
    :generations-id "",
    :id "Cell Wall Zwei Example",
    :rule "(function [left old right] \n   (or left right))"}}
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :all-transform,
    :z 4,
    :init-id "",
    :x 8,
    :precompute? true,
    :rule-id "",
    :target-id "",
    :row-id "",
    :pair-id "",
    :generations-id "",
    :id "Cell Wall Eins Example",
    :rule "(function [left old right] \n   (not old))"}}
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :rower,
    :z 4,
    :init-id "",
    :x 10,
    :precompute? false,
    :rule-id "",
    :target-id "Door Eins",
    :row-id "",
    :pair-id "Cell Wall Eins Example",
    :generations-id "",
    :id "Cell Wall Eins",
    :rule "(function [left old right] \n   (not old))"}}
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :rower,
    :z 4,
    :init-id "",
    :x 11,
    :precompute? false,
    :rule-id "",
    :target-id "Door Zwei",
    :row-id "",
    :pair-id "Cell Wall Zwei Example",
    :generations-id "",
    :id "Cell Wall Zwei",
    :rule "(function [left old right] \n   (or left right))"}}
  {:name :glass-door,
   :answers
   {:z 5,
    :x 9,
    :id "Door Eins",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 7.0}}
  {:name :text-screen,
   :answers
   {:direction -0.010849483861758383,
    :protocol :menus,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND WHICH OPTION YOU WOULD LIKE TO VIEW",
    :parameter "choice",
    :text-color {:red 127, :green 255, :blue 0},
    :z 6,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform
    "{:start 0,\n 0\n {:label \"THE FOLLOWING FUNCTIONS MAY BE HELPFUL\",\n  :states\n  [{:state 1, :label \"Recursive Functions\"}\n   {:state 2, :label \"iterate\"}]},\n 1\n {:label \"YOU CAN SAVE FUNCTIONS WITH NAMES (WE CALL THESE FUNCTIONS 'RECURSIVE' OR 'KIND OF SELF-CENTERED LITTLE BUGGERS'). HERE'S AN EXAMPLE FUNCTION:\\n\\nFUNCTION NAME: fibonacci\\nPARAMETERS:n\\nCODE:(if (less-than? n 2)\\n  1\\n  (add (fibonacci (dec n)) (fibonacci (dec (dec n)))))\"\n  :states [{:state 0, :label \"Go Back\"}]},\n 2\n {:label\n  \"iterate TAKES AN INITIAL VALUE AND A FUNCTION AND RETURNS AN INFINITE LIST OF APPLYING THE FUNCTION OVER AND OVER AGAIN.\\n\\n(iterate inc 1) RETURNS [1 2 3 4 ...]\\n\\n(iterate not true) RETURNS [true false true false true false ....]\",\n  :states [{:state 0, :label \"Go Back\"}]}}\n",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring "",
    :distance 40,
    :id "Help Screen Eins",
    :target-ids []}}
  {:name :glass-door,
   :answers
   {:z 6,
    :x 9,
    :id "Door Zwei",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :right,
    :speed 2.0,
    :time 7.0}}
  {:name :text-screen,
   :answers
   {:direction -0.039714206833238896,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT TAKES AN INTEGER AND RETURNS THE INTEGER TO THE FIFTH POWER.\n\nEXAMPLES:\n\n(fn 2) RETURNS 32\n\n(fn -5) RETURNS -3125\n ",
    :parameter "fn",
    :text-color {:red 127, :green 255, :blue 0},
    :z 7,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "id",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: a 1-argument function called fn.\nReturns: Modifies what the cannons spit using fn.",
    :distance 40,
    :id "Transformer Drei",
    :target-ids []}}
  {:name :function-cannon,
   :answers
   {:direction -1.5707963267948966,
    :init-queue "(repeat 3)",
    :constraint "",
    :z 9,
    :velocity 75,
    :x 3,
    :transform-id "Transformer Drei",
    :interactive? true,
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon Drei"}}
  {:name :function-cannon,
   :answers
   {:direction -1.5707963267948966,
    :init-queue "(repeat -3)",
    :constraint "",
    :z 9,
    :velocity 75,
    :x 5,
    :transform-id "Transformer Drei",
    :interactive? true,
    :mass 0.5,
    :queue? true,
    :distance 24,
    :id "Cannon Vier"}}
  {:name :globule-receiver,
   :answers
   {:z 11,
    :x 3,
    :id "Hoop Drei",
    :direction 1.5707963267948966,
    :y 1,
    :target-ids ["Door Drei"],
    :protocol :open-on-pattern,
    :pattern ["243"]}}
  {:name :globule-receiver,
   :answers
   {:z 11,
    :x 5,
    :id "Hoop Vier",
    :direction 1.5707963267948966,
    :y 1,
    :target-ids ["Door Vier"],
    :protocol :open-on-pattern,
    :pattern ["-243"]}}
  {:name :glass-door,
   :answers
   {:z 11,
    :x 16,
    :id "Exit Door",
    :direction -1.5316007248678418,
    :distance 8.0,
    :movement :up,
    :speed 5.0,
    :time 0.0}}
  {:name :glass-door,
   :answers
   {:z 12,
    :x 4,
    :id "Door Drei",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 3.0}}
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 12,
    :init-id "",
    :x 10,
    :precompute? false,
    :rule-id "",
    :target-id "Door Sieben",
    :row-id "",
    :pair-id "Cell Wall Sieben Example",
    :generations-id "",
    :id "Cell Wall Sieben",
    :rule
    "(function [left old right] \n   (and (any? not [left old right])\n           (any? id [left old right])))"}}
  {:name :text-screen,
   :answers
   {:direction -1.5750335895711434,
    :protocol :open,
    :text "POGGIO INSTITUTE\n================\nSEND true TO EXIT.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 12,
    :x 15,
    :success-text "\nGOOD BYE.",
    :font-size 0.6,
    :split? false,
    :transform "(function [x] x)",
    :end-level? true,
    :success? "message",
    :error-text "Just send true!",
    :docstring
    "Takes: A boolean called message.\nReturns: Opens the door when message is true.",
    :distance 40,
    :id "Exit Transformer",
    :target-ids ["Exit Door"]}}
  {:name :text-screen,
   :answers
   {:direction -1.5490606199531038,
    :protocol :none,
    :text "EXIT",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 12,
    :x 17,
    :success-text "",
    :font-size 2.0,
    :split? false,
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
   {:z 13,
    :x 4,
    :id "Door Vier",
    :direction 1.5707963267948966,
    :distance 8.0,
    :movement :right,
    :speed 2.0,
    :time 3.0}}
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 13,
    :init-id "",
    :x 10,
    :precompute? false,
    :rule-id "",
    :target-id "Door Sechs",
    :row-id "",
    :pair-id "Cell Wall Sechs Example",
    :generations-id "",
    :id "Cell Wall Sechs",
    :rule "(function [left old right] \n   (or left right))"}}
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 14,
    :init-id "",
    :x 10,
    :precompute? false,
    :rule-id "",
    :target-id "Door Funf",
    :row-id "",
    :pair-id "Cell Wall Funf Example",
    :generations-id "",
    :id "Cell Wall Funf",
    :rule "(function [left old right] \n   (not old))"}}
  {:name :text-screen,
   :answers
   {:direction 1.4596855909128643,
    :protocol :pass,
    :text
    "POGGIO INSTITUTE\n================\n\nSEND A FUNCTION THAT TAKES IN A ROW-GENERATING FUNCTION (LIKE THE ONE YOU MADE EARLIER), A RULE ,AND  A ROW OF CELLS AND COMPUTES A LIST OF GENERATIONS STARTING WITH THE GIVEN ROW (THIS LIST CAN BE INFINITE).\n\nEXAMPLE:\n\n(generator\n  (function [rule init]\n    init)\n  (function [x y z]\n     (not y))\n   [false false true false false])\n\nRETURNS\n[false false true false false]\n\n\n\n",
    :parameter "generator",
    :text-color {:red 127, :green 255, :blue 0},
    :z 15,
    :x 7,
    :success-text "",
    :font-size 0.6,
    :split? false,
    :transform "(function [x] x)",
    :end-level? false,
    :success? "",
    :error-text "",
    :docstring
    "Takes: A 3-argument function called generator.\nReturns: Builds the cell walls using the generator.",
    :distance 40,
    :id "Transformer Eins",
    :target-ids
    ["Cell Wall Funf" "Cell Wall Sechs" "Cell Wall Sieben"]}}
  {:name :glass-door,
   :answers
   {:z 15,
    :x 11,
    :id "Door Funf",
    :direction -3.0845118711835284,
    :distance 8.0,
    :movement :down,
    :speed 2.0,
    :time 3.0}}
  {:name :glass-door,
   :answers
   {:z 15,
    :x 12,
    :id "Door Sechs",
    :direction -3.0738996308958244,
    :distance 8.0,
    :movement :left,
    :speed 2.0,
    :time 3.0}}
  {:name :glass-door,
   :answers
   {:z 15,
    :x 13,
    :id "Door Sieben",
    :direction -3.141592653589793,
    :distance 8.0,
    :movement :right,
    :speed 2.0,
    :time 3.0}}
  {:name :text-screen,
   :answers
   {:direction 3.1345256334318194,
    :protocol :none,
    :text
    "POGGIO INSTITUTE\n================\n\nHMM....YOUR SKILL HAS GROWN CONSIDERABLY, MONKEY.",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 15,
    :x 16,
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
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 16,
    :init-id "",
    :x 10,
    :precompute? true,
    :rule-id "",
    :target-id "Door Funf",
    :row-id "",
    :pair-id "Cell Wall Funf Example",
    :generations-id "",
    :id "Cell Wall Funf Example",
    :rule "(function [left old right] \n   (not old))"}}
  {:name :glass-door,
   :answers
   {:z 16,
    :x 14,
    :id "",
    :direction 1.5707963267948966,
    :distance 0.0,
    :movement :up,
    :speed 0.0,
    :time 0.0}}
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 17,
    :init-id "",
    :x 10,
    :precompute? true,
    :rule-id "",
    :target-id "Door Sechs",
    :row-id "",
    :pair-id "Cell Wall Sechs Example",
    :generations-id "",
    :id "Cell Wall Sechs Example",
    :rule "(function [left old right] \n   (or left right))"}}
  {:name :model,
   :answers
   {:z-delta 8.0,
    :direction -1.5707963267948966,
    :model-name "Models/Penguin/Pingouin-NEW.scene",
    :z 17,
    :x 14,
    :y-delta 0.0,
    :x-delta 0.0,
    :id "",
    :collidable? false}}
  {:name :cellular-automaton,
   :answers
   {:direction -3.0771658141642395,
    :protocol :generator,
    :z 18,
    :init-id "",
    :x 10,
    :precompute? true,
    :rule-id "",
    :target-id "Door Sieben",
    :row-id "",
    :pair-id "Cell Wall Sieben Example",
    :generations-id "",
    :id "Cell Wall Sieben Example",
    :rule
    "(function [left old right] \n   (and (any? not [left old right])\n           (any? id [left old right])))"}}
  {:name :cellular-automaton,
   :answers
   {:direction 1.5707963267948966,
    :protocol :all-transform,
    :z 18,
    :init-id "",
    :x 14,
    :precompute? true,
    :rule-id "",
    :target-id "",
    :row-id "",
    :pair-id "",
    :generations-id "",
    :id "",
    :rule
    "(function [left old right] \n   (any? id [left old right]))"}}]}
