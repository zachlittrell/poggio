(ns tools.level-editor.widgets.utilities
  (:use [control assert bindings timer]
        [data coll color object]
        [jme-clj control selector transform]
        [poggio.functions core modules utilities color scenegraph]))

(defn do-list-timer 
  "Returns a control-timer for spatial that iterates through xs.
   It uses env to realize the value of the list, and transform to 
   do any transformation on the values of xs. If the value doesn't match
   type, an error is thrown. Every time a value passes all tests,
   it is passed to on-success. If there are no more values,
   on-empty is called. Else, the error is passed to on-failure.
   Every time a new timer is started, handle-timer! is called."
  [spatial xs type env transform 
   on-value! on-empty! on-failure! handle-timer!]
  (let [*xs* (atom xs)]
    (control-timer spatial 0.5 false
      (fn []
        (let [comp-timer
                (computation-timer spatial
                   5
                  (fn []
                    (let [xs (value @*xs* env)]
                      (assert! (implements? clojure.lang.Seqable xs))
                      (when-not (empty? xs)
                        (let [x (transform (first xs))
                              xs* (rest xs)]
                          (assert! (implements? type x))
                          [x xs*]))))
                  (fn [result]
                    (if-let [[x xs] (seq result)]
                      (do
                        (on-value! x
                        #(let [new-timer (do-list-timer spatial
                                                       xs
                                                       type
                                                       env
                                                       transform
                                                       on-value!
                                                       on-empty!
                                                       on-failure!
                                                       handle-timer!)]
                          (handle-timer! new-timer)
                          (start! new-timer))))
                      (on-empty!)))
                  on-failure!)]
          (handle-timer! comp-timer)
          (start! comp-timer))))))


(defn do-list-pog-fn 
  [& {:keys [spatial 
           on-value! queue? transformer-id valid-input-type queue-init param
           handle-continuation?
           app]
    :or {param "globules"
         valid-input-type Object}}]
  (let [*state* (atom {:state :inactive})
        *queue* (atom [])
        on-value! (if handle-continuation? 
                    on-value! 
                    #(do (on-value! %1) (%2)))
        [my-error! transformer]
          (if (empty? transformer-id)
            [(constantly nil) identity]
            [#(on-bad-transform! (spatial-pog-fn (select app transformer-id)))
             #(transform (spatial-pog-fn (select app transformer-id)) %)])]
  (reify
    LazyPogFn
    (lazy-invoke [_ env {player "player"
                         on-error!  "on-error!"
                         balls param}]
      (if (and player (< 16
                         (.distance (.getPhysicsLocation player)
                                    (.getWorldTranslation spatial))))
        (on-error! (Exception. "You must be closer to interact with this."))
        (let [state @*state*]
          (when (= (:state state) :active)
            (if queue?
              (swap! *queue* concat balls)
              (stop! (:timer state))))
          (when-not (and (= (:state state) :active)
                         queue?)
            (let [timer (do-list-timer spatial balls valid-input-type
                                       env transformer on-value!
                                       (fn []
                                        ;;TODO HERE we'll handle emptiness 
                                         )
                                       (fn [error]
                                         (my-error! error)
                                         (on-error! error)
                                         (reset! *state* {:state :inactive}))
                                       (fn [timer]
                                         (reset! *state*
                                                 {:state :active
                                                  :timer timer})))]
              (reset! *state* {:state :active :timer timer})
              (start! timer))))))
    PogFn
    (parameters [_]
      (|_|?
         (when-not queue?
           {:name "player"
            :type Warpable})
         {:name "on-error!"
          :type clojure.lang.IFn}
         {:name param
          :type clojure.lang.Seqable}))
    (docstring [_]
      docstring))))


