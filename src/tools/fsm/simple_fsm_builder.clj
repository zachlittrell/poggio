(ns tools.fsm.simple-fsm-builder
  (:use [data coll string]
        [seesaw core]))

(defn map->fsm 
  [{:keys [main children]}]
 (apply array-map
   (apply concat
     (list* 
         [:start 0]
         [0 {:label main 
             :states 
               (vec
                 (for [[i {:keys [title]}] (index 1 children)]
                 {:state i :label title} 
                 ))}]
         (for [[i {:keys [title text]}] (index 1 children)]
           [i {:label (str title "\n\n" text)
               :states [{:state 0 :label "Go Back"}] }])))))

(defn display-fsm!
  [map]
  (-> (frame :on-close :exit
             :size [200 :by 200]
             :content (border-panel :center (scrollable
                                              (text :multi-line? true
                                                    :text 
                                                    (object->pretty-printed-string
                                                      (map->fsm map))))))
    (show!)))

