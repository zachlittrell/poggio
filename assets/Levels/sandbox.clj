{:loc [2 1],
 :dir -1.5707963267948966,
 :walls
 #{[6 5] [1 0] [6 6] [0 0] [4 5] [6 7] [0 1] [3 5] [6 8] [0 2] [2 5]
   [0 3] [5 9] [0 4] [1 5] [4 9] [1 6] [3 9] [1 7] [2 9] [1 8] [1 9]
   [6 0] [5 0] [6 1] [4 0] [6 2] [3 0] [6 3] [6 4] [2 0]},
 :wall-mat "Textures/paper1.jpg",
 :widgets
 [{:name :function-cannon,
   :answers
   {:direction -1.5707963267948966,
    :constraint "",
    :z 2,
    :velocity 75,
    :x 2,
    :transform-id "",
    :mass 0.5,
    :queue? false,
    :id ""}}
  {:name :globule-receiver,
   :answers
   {:z 4,
    :x 2,
    :id "",
    :direction 1.5518112662960561,
    :y 2,
    :target-ids ["cannon2"],
    :protocol :pass,
    :pattern []}}
  {:name :text-screen,
   :answers
   {:direction 1.5707963267948966,
    :protocol :hold,
    :text
    "POGGIO INSTITUTE\n================\n\nTHIS SCREEN CONTROLS HOW WE CHANGE THE BALL\n\n",
    :parameter "message",
    :text-color {:red 127, :green 255, :blue 0},
    :z 4,
    :x 3,
    :success-text "",
    :font-size 0.6,
    :transform
    "(function x\n  (if (equal? x 1)\n    red\n    (if (equal? x 2)\n      green\n      blue)))",
    :end-level? false,
    :target-id "",
    :success? "",
    :error-text "",
    :docstring "",
    :id "trans"}}
  {:name :function-cannon,
   :answers
   {:direction 1.5707963267948966,
    :init-queue "",
    :constraint "",
    :z 4,
    :velocity 75,
    :x 4,
    :transform-id "trans",
    :mass 0.5,
    :queue? true,
    :id "cannon2"}}
  {:name :sound-barrier,
   :answers
   {:z 5,
    :x 5,
    :id "",
    :direction -1.5835344912849612,
    :target-id "Music Box"}}
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
  {:name :ball-stereo,
   :answers
   {:z 7,
    :x 2,
    :id "stereo8",
    :target-ids [],
    :protocol :open-on-pattern}}
  {:name :music-box,
   :answers
   {:z 7,
    :x 3,
    :id "Music Box",
    :direction 1.5707963267948966,
    :stereos
    ["stereo1"
     "stereo2"
     "stereo3"
     "stereo4"
     "stereo5"
     "stereo6"
     "stereo7"
     "stereo8"],
    :transformer-id "",
    :queue? false,
    :queue-init ""}}
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
    :protocol :open-on-pattern}}]}
