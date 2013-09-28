{:loc [2 1],
 :dir -1.5707963267948966,
 :walls
 #{[1 0] [5 5] [0 0] [4 5] [0 1] [3 5] [0 2] [2 5] [0 3] [0 4] [1 5]
   [6 0] [5 0] [6 1] [4 0] [6 2] [3 0] [6 3] [6 4] [2 0]},
 :wall-mat "Textures/paper1.jpg",
 :widgets
 [{:name :function-cannon,
   :answers
   {:z 2,
    :x 2,
    :id "",
    :direction -1.5707963267948966,
    :velocity 75,
    :mass 0.5,
    :transform-id "",
    :queue? false,
    :constraint ""}}
  {:name :globule-receiver,
   :answers
   {:z 4,
    :x 2,
    :id "",
    :direction 1.5518112662960561,
    :y 2,
    :target-id "cannon2",
    :protocol :pass,
    :pattern []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nTHIS SCREEN CONTROLS HOW WE CHANGE THE BALL\n\n",
    :parameter "message",
    :z 4,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :transform-param "x",
    :transform
    "if (equal? x 1)\n  red\n  (if (equal? x 2)\n    green\n    blue)",
    :end-level? false,
    :target-id "",
    :success? "",
    :error-text "",
    :docstring "",
    :id "trans"}}
  {:name :function-cannon,
   :answers
   {:z 4,
    :x 4,
    :id "cannon2",
    :direction 1.5707963267948966,
    :velocity 75,
    :mass 0.5,
    :transform-id "trans",
    :queue? true,
    :constraint ""}}]}
