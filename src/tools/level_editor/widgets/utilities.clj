(ns tools.level-editor.widgets.utilities
  (:import [com.jme3.math ColorRGBA Vector3f])
  (:require [clojure.string :as str])
  (:use [control assert bindings timer]
        [data coll color object ring-buffer]
        [jme-clj control selector transform physics]
        [poggio.functions core modules parser utilities color scenegraph]))

(defn too-far? 
  ([spatial player]
   (too-far? spatial player 24))
  ([spatial player distance]
   (< distance (.distance ^Vector3f (location player) 
                          ^Vector3f (location spatial)))))


(defmacro when-close-enough 
  [on-error! player spatial distance & body]
  `(let [player# ~player
         spatial# ~spatial
         distance# ~distance]
     (if (and player# (pos? distance#) (too-far? spatial# player# distance#))
       (~on-error! (Exception. "You must be closer to interact with this."))
       (do
         ~@body))))

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
                  (fn [error] (on-failure! error @*xs*)))]
          (handle-timer! comp-timer)
          (start! comp-timer))))))


(defn do-list-pog-fn 
  [& {:keys [spatial 
             wait-time
             init-wait-time
           on-value! queue? transformer-id transformer valid-input-type queue-init param
             on-error!
             on-invoke!
             interactive?
             distance
           handle-continuation?
           app]
    :or {param "globules"
         transformer identity
         distance 24
         valid-input-type Object}}]
  (let [*state* (atom {:state :inactive})
        *queue* (atom [])
        wait-time (if wait-time wait-time (constantly init-wait-time))
        on-value! (if handle-continuation? 
                    on-value! 
                    #(do (on-value! %1) (%2)))
        [my-error! transformer]
          (if (empty? transformer-id)
            [(constantly nil) transformer]
            [#(on-bad-transform! (spatial-pog-fn (select app transformer-id)))
             #(transform (spatial-pog-fn (select app transformer-id)) %)])
        pog-fn
  (reify
    LazyPogFn
    (lazy-invoke [_ env {player "player"
                         balls param}]
      (when-close-enough on-error! player spatial distance
        (let [state @*state*]
          (when (= (:state state) :active)
            (if queue?
              (swap! *queue* concat balls)
              (stop! (:timer state))))
          (when-not (and (= (:state state) :active)
                         queue?)
            (when on-invoke! (on-invoke!))
            (let [on-empty!  (fn [new-timer]
                              (let [queue @*queue*]
                                (if (empty? queue)
                                  (reset! *state* {:state :inactive})
                                  (do 
                                    (reset! *queue* [])
                                    (let [timer (new-timer queue)]
                                      (reset! *state* {:state :active
                                                       :timer timer})
                                      (start! timer))))))
 
                  new-timer (fn new-timer [balls]
                             (do-list-timer spatial balls valid-input-type
                                       env transformer
                                       init-wait-time
                                       wait-time
                                       on-value!
                                       (partial on-empty! new-timer) 
                                       (fn [error leftovers]
                                         (my-error!)
                                         (on-error! error)
                                         (if queue?
                                           (reset! *queue*
                                                   (concat leftovers @*queue*)))
                                         (on-empty! new-timer)
                                        ); (reset! *state* {:state :inactive}))
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
         (when-not (or queue? (false? interactive?))
           {:name "player"
            :type Warpable})
         {:name "on-error!"
          :type clojure.lang.IFn}
         {:name param
          :type clojure.lang.Seqable}))
    (docstring [_]
      (if-not queue?
        (docstr [["values" "a list of values"]]
            (str "Spits out a globule for each value in globules."))
        ;;TODO add string explicitly stating constraints on cannon.
        (str "Spits out a globule for each value passed to it."
          (if (empty? queue-init) ""
            (str " Starts with the values given by " queue-init ".")))
        )))]
    pog-fn
    ))

(defn start-do-list!?
  [spatial queue? queue-init]
  (when (and queue? (not-empty queue-init))
    (do-once! spatial
      (invoke* (spatial-pog-fn spatial)
               core-env
               [nil (code-pog-fn [] "" queue-init)]))))

(defn str->encoding [^String s]
  (condp re-matches s
    #"-?(\d+)(.\d+)?" (bigdec s)
    #"red" ColorRGBA/Red
    #"green" ColorRGBA/Green
    #"blue" ColorRGBA/Blue
    (if-let [[_ r g b] (re-matches #"color (\d+) (\d+) (\d+)" s)]
      (ColorRGBA. (float (/ (bigdec r) 255))
                  (float (/ (bigdec g) 255))
                  (float (/ (bigdec b) 255))
                  1.0))))

(defprotocol Equable
  (equal? [eq obj]))

(extend-protocol Equable
  Number
  (equal? [n1 n2]
    (== n1 n2))
  ColorRGBA
  (equal? [c1 c2]
    (rgb-equal? c1 c2))
  clojure.lang.Seqable
  (equal? [seq1 seq2]
    (if (empty? seq1)
      (empty? seq2)
      (if (empty? seq2)
        false
        (when (equal? (first seq1)
                      (first seq2))
          (recur (next seq1) (next seq2)))))))

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
            (invoke* target (case (count (parameters target))
                              1 [val*]
                              2 [nil val*]
                              3 [nil nil val*]))))))))


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



(defprotocol Stringable
  (to-str [s]))
(declare str**)
(declare str*)
(extend-protocol Stringable
  Object
  (to-str [o] (str o))
  ColorRGBA
  (to-str [c] 
      (format "(color %s %s %s)"
        (* 255 (red c)) (* 255 (green c)) 
        (* 255 (blue c))))
  clojure.lang.Seqable
  (to-str [s]
    (concat ["["]
            (flatten (interpose " " (map str* s)))
            ["]"])))

(defn str* 
  [o]
  (seq (str** o)))
(defn str** [o]
  (if (and (is-pog-fn? o) (> (count (parameters o)) 0))
     (format "<%s-parameter function>"
             (count (parameters o)))
    (to-str o)))


