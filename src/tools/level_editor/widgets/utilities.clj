(ns tools.level-editor.widgets.utilities
  (:import [com.jme3.math ColorRGBA])
  (:require [clojure.string :as str])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer]
        [jme-clj control selector transform physics]
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
   init-wait-time
   wait-time on-value! on-empty! on-failure! handle-timer!]
  (let [*xs* (atom xs)]
    (control-timer spatial init-wait-time false
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
                                                       (wait-time x)
                                                       wait-time
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
             wait-time
             init-wait-time
           on-value! queue? transformer-id valid-input-type queue-init param
           handle-continuation?
           app]
    :or {param "globules"
         valid-input-type Object}}]
  (let [*state* (atom {:state :inactive})
        *queue* (atom [])
        wait-time (if wait-time wait-time (constantly init-wait-time))
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
            (let [new-timer (fn new-timer [balls]
                             (do-list-timer spatial balls valid-input-type
                                       env transformer
                                       init-wait-time
                                       wait-time
                                       on-value!
                                       (fn []
                                        (let [queue @*queue*]
                                          (if (empty? queue)
                                            (reset! *state* {:state :inactive})
                                            (do 
                                              (reset! *queue* [])
                                              (let [timer (new-timer queue)]
                                                (reset! *state* {:state :active
                                                                 :timer timer})
                                                (start! timer))))))
                                         
                                       (fn [error]
                                         (my-error! error)
                                         (on-error! error)
                                         (reset! *state* {:state :inactive}))
                                       (fn [timer]
                                         (reset! *state*
                                                 {:state :active
                                                  :timer timer}))))
                  timer (new-timer balls)]
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

(defn str->encoding [^String s]
  (condp re-matches s
    #"-?(\d+)(.\d+)?" (bigdec s)
    #"red" ColorRGBA/Red
    #"green" ColorRGBA/Green
    #"blue" ColorRGBA/Blue))

(defprotocol Equable
  (equal? [eq obj]))

(extend-protocol Equable
  clojure.lang.Seqable
  (equal? [seq1 seq2]
    (when (and (not-empty seq1) (not-empty seq2)
               (equal? (first seq1) (first seq2)))
      (recur (next seq1) (next seq2)))))

(defn open-on-pattern-processor [target-ids pattern]
  (let [*buffer* (atom (ring-buffer (count pattern)))
        encoded-pattern (map str->encoding (when-not (empty? pattern)
                                             (rseq pattern)))]
    {:docstring (format "Activates %s once it receives %s."
                        (str/join "," target-ids)
                        (str/join "," pattern))
     :process! (partial swap! *buffer* adjoin )
     :proceed? #(equal? encoded-pattern (lifo @*buffer*))
     :transform (constantly true)}))

(defn pass-processor [target-ids]
  {:docstring (format "Passes globules to %s." (str/join "," target-ids))
   :process! (constantly nil)
   :proceed? (constantly true)
   :transform list})

(defn process-globule! [app receiver process! proceed? transform target-ids globule]
  (detach! (:globule globule))
    (let [val (value (:value globule) {})]
      (when (implements? ColorRGBA val)
        (.setColor (.getMaterial receiver) "Color" val))
     (process! val)
    (if (proceed?)
      (let [val* (transform val)]
        (doseq [target-id target-ids]
          (when-let [target (spatial-pog-fn (select app target-id))]
            (invoke* target (if (== 1 (count (parameters target)))
                              [val*]
                              [nil val*]))))))))


(defn globule-processor-pog-fn
  [& {:keys [app spatial target-ids
             process! proceed? transform
              docstring]}]
  (fn->pog-fn (partial process-globule! app spatial 
                       process! proceed? transform target-ids)
              "processor"
              ["globule"]
              docstring))

(defn keyword->globule-processor-pog-fn
  [& {:keys [app spatial target-ids pattern keyword]}]
   (let [processor (case keyword
                     :open-on-pattern (open-on-pattern-processor
                                        target-ids pattern)
                     :pass (pass-processor target-ids))]
     (globule-processor-pog-fn :app app
                               :spatial spatial
                               :target-ids target-ids
                               :process! (:process! processor)
                               :proceed? (:proceed? processor)
                               :transform (:transform processor)
                               :docstring (:docstring processor))))
