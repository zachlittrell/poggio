(ns poggio.functions.gui
  (:import [de.lessvoid.nifty.screen DefaultScreenController])
  (:require [clojure.string :as str]
            [data.map :as map]
            [nifty-clj [builders :as builders]]) 
  (:use [clojure.java io]
        [data coll object]
        [nifty-clj [builders :exclude [text]] elements events textfieldx]
        [poggio.functions core parser modules]))

(def function-save-file "user-functions")
(defn save-user-functions!
  [fn-map]
  (future
    (locking function-save-file
      (try
      (spit function-save-file
            (into {}
              (for [
                    [module fns] fn-map
                    :let [fns* (do
                                 (map/difference fns (core-modules module)))]
                    :when (not-empty fns*)]
                [module (into {} (map/value-map 
                                   fns* 
                                   #(array-map
                                      :parameters (parameters %)
                                      :docstring (docstring %)
                                      :source-code (source-code %))))])))
        
       (catch Exception e (.printStackTrace e) )))))

(defn load-user-functions! [*fn-map*]
  (locking function-save-file
    (when (.exists (file function-save-file))
      (loop [fn-map @*fn-map*
             user-map (read-string (slurp function-save-file))]
        (if-let [[[module fns] & more] (seq user-map)]
          (recur (update-in fn-map [module] 
                            (partial merge 
                                     (map/value-map fns
                                        #(code-pog-fn (:parameters %)
                                                      (:docstring %)
                                                      (:source-code %)))))
                 more)
          (reset! *fn-map* fn-map))))))

(defn clean-all! [screen nifty]
  (.hideWithoutEffect (select screen "alert-panel")) 
  (.hideWithoutEffect (select screen "fn-build-pad"))
  (.hideWithoutEffect (select screen "fn-panels"))
  (set-lines! nifty (select screen "fn-pad") ""))


(defn alert! [screen text]
  (let [alert-panel (select screen "alert-panel")
        alert-text (select screen "alert-text")
        text (if (string? text) text (.getMessage text))]
    (.setVisible alert-panel false)
    (set-text! alert-text (str text "\nClick to close"))
    (.layoutElements (.getParent alert-panel))
    ;(.setHeight alert-panel (.getHeight alert-text))
    (.setVisible alert-panel true)))

(defn set-pog-fn! [fn-pad-param nifty f name]
  (set-text! fn-pad-param 
             (format "%s: %s"
                     name
                     (name* (first (parameters f))))))

(defn get-pog-fn [fn-pad f fn-map ]
  (seq->pog-fn "" [] 
    (list f (code-pog-fn [] "" (lines (first-entry fn-pad))))))

(defn invalidate! 
  ([screen module fn-name]
   (invalidate! screen module fn-name false))
  ([screen module fn-name add-back?]
    (let [modules (nifty-control (select screen "pog-mods-drop") :drop-down)
          functions (nifty-control (select screen "pog-fns-drop") :drop-down)]
      (.removeItem functions fn-name)
      (when (and add-back? (some #(= fn-name %) (.getItems functions)))
                 (.addItem functions fn-name)))))
    
(defn modfn [screen]
  (let [modules (nifty-control (select screen "pog-mods-drop") :drop-down)
        functions (nifty-control (select screen "pog-fns-drop") :drop-down)]
    (if (== (.getSelectedIndex modules) 1)
      (when-not (zero? (.getSelectedIndex functions))
        (let [search-result (.getSelection functions)]
          (path-components search-result)))
      (when (and (> (.getSelectedIndex modules) 1)
                 (> (.getSelectedIndex functions) 0))
        ;;TODO
        ;;allow searched functions to be editted
        [(.getSelection modules) (.getSelection functions)]))))

(defn function-info []
  {:info (tabs
           :width "100%"
           :height "100%"
           :background-color "#ffffff00"
           :tabs [(tab :id "doc" :caption "Doc"
                       :background-color "#ffffff00"
                       :width "100%"
                       :height "100%"
                       :control (scroll-panel
                                  :background-color "#00000000"
                                  :id "doc-scroller"
                                  :width "100%"
                                  :set  {"horizontal" "false"}
                                  :height "100%"
                                  :style "nifty-listbox"
                                  :control (label :text "Documentation"
                                                  :id "doc-label"
                                                  :align :left
                                                  :width "100%" :height "100%" :text-h-align :left
                                                  :text-v-align :top
                                                  :wrap? true)))
                  (tab :id "source" :caption "Source"
                       :background-color "#ffffff00"
                       :width "100%"
                       :height "100%"
                       :control (scroll-panel
                                  :background-color "#00000000"
                                  :id "source-scroller"
                                  :width "100%"
                                  :set  {"horizontal" "false"}
                                  :height "100%"
                                  :style "nifty-listbox"
                                  :control (label :text "Source Code"
                                                  :id "source-label"
                                                  :align :left
                                                  :width "100%" :height "100%" :text-h-align :left
                                                  :text-v-align :top
                                                  :wrap? true)))

                  ])
   :update! (fn [info f]
                  (let [scroll (select info "#nifty-scrollpanel-child-root")]
                     (set-text! (select info "source-label") 
                      (cond (nil? f) ""
                            (implements? Sourceable f) 
                              (str
                                "Parameters: " (str/join "," 
                                                      (map name* (parameters f)))
                                "\n\n"
                                (or (source-code f) ""))
                            :else 
                              (str
                                "Parameters: " (str/join "," 
                                                     (map name* (parameters f)))
                                "\n\n"
                                "Source code is unavailable.")))

                    (set-text! (select info "doc-label") 
                               (if f (docstring f)
                                     ""))
                    (.layoutElements scroll)
                    (.setUp (nifty-control info :scroll) 0 20 0 200 de.lessvoid.nifty.controls.ScrollPanel$AutoScroll/OFF)))})

(defn function-box [& modules]
  (let [{:keys [update! info]} (function-info)
        *search* (atom [])
        box (panel :child-layout :vertical
                   :padding "1px"
                   :width "100%"
                   :panels 
                   [(panel :child-layout :vertical
                           :controls 
                             [(drop-down :id "pog-mods-drop"
                                         :width "100%")
                              (drop-down :id "pog-fns-drop"
                                         :width "100%")
                              (text-field :id "pog-fns-search"
                                          :width "100%")])
                    (panel :id "pog-fns-info"
                           :child-layout :vertical
                           :control info)])
        fn-map (into {} modules)
        *fn-map* (atom fn-map)]
  {:*fn-map* *fn-map*
   :fn-box box
   :selected-fn! (fn [nifty screen]
                   (let [modules (-> screen
                                     (select , "pog-mods-drop")
                                     (nifty-control :drop-down))
                         functions (-> screen
                                       (select , "pog-fns-drop")
                                       (nifty-control :drop-down))]
                     (cond  (== (.getSelectedIndex modules) 1)
                              (.getSelection functions)
                            (> (.getSelectedIndex functions) 0)
                             (path (.getSelection modules)
                                   (.getSelection functions)))))
                           

   :initialize! (fn [nifty screen]
                  (let [modules (-> screen
                                     (select , "pog-mods-drop")
                                     (nifty-control :drop-down))]
                   (.addItem modules "Modules")
                   (.addItem modules "Search...")
                   (.addAllItems modules
                                 (sort (keys fn-map))))
                  (.addAllItems (-> screen
                                  (select , "fn-build-modules")
                                  (nifty-control :drop-down))
                                (sort (keys fn-map)))
                  (subscribe! nifty "pog-mods-drop" :drop-down-select
                    (fn [topic data]
                      (cond
                        ;;If there is either no items or 
                        ;;We are at the 'Modules' label
                        ;;Just clear out
                        (<= (.getSelectionItemIndex data) 0)
                          (do (.clear (-> screen (select , "pog-fns-drop")
                                                 (nifty-control :drop-down))))
                        ;;If we are on the Search label
                        ;;Add the search items
                        (== (.getSelectionItemIndex data) 1)
                        (let [functions (-> screen (select , "pog-fns-drop")
                                                   (nifty-control :drop-down))]
                          (doto functions
                            (.clear)
                            (.addItem "Functions")
                            (.addAllItems (sort @*search*))))

                          
                        :else
                        (do
                        (let [module (.getSelection data)
                              functions (-> screen
                                            (select , "pog-fns-drop")
                                            (nifty-control :drop-down))]
                          (.clear functions)
                          (.addItem functions "Functions")
                          (.addAllItems functions
                                       (sort (map first (@*fn-map* module)))))))))

                  (subscribe! nifty "pog-fns-drop" :drop-down-select
                    (fn [topic data]
                      (if (<= (.getSelectionItemIndex data) 0)
                        (update! (first (.getElements 
                                          (select screen "pog-fns-info"))) nil)
                        (do
                        (let [modules (-> screen
                                          (select , "pog-mods-drop")
                                          (nifty-control :drop-down))
                              [module fn-name] 
                                (if (== (.getSelectedIndex modules) 1)
                                  (path-components (.getSelection data))
                                  [(.getSelection modules)
                                   (.getSelection data)])
                              f ((@*fn-map* module) fn-name)
                              info-pane (select screen "pog-fns-info")]
                         (update! (first (.getElements info-pane)) f))))))

                  (subscribe! nifty "pog-fns-search" :text-field-change
                    (fn [topic data]
                      (let [text (str/lower-case (.getText data))
                            matches (for [[module functions] @*fn-map*
                                          [function _] functions
                                          :when (< -1 
                                                   (.indexOf (str/lower-case function)
                                                             text))]
                                      (path module function))
                            modules (-> (select screen "pog-mods-drop")
                                        (nifty-control :drop-down))
                            functions (-> (select screen "pog-fns-drop")
                                          (nifty-control :drop-down))]

                      (swap! *search* (constantly matches))
                      (.selectItemByIndex modules 1)
                      (when (not-empty matches)
                        (.selectItemByIndex functions 1))))))
   :clean!       (fn [])}))


(defn function-screen [nifty & modules]
  (let [{:keys[*fn-map* fn-box initialize! clean!
               selected-fn!]} (apply function-box modules)
        {initialize2! :initialize!
         pad-textfieldx :textfieldx} (textfieldx :id "fn-pad"
                                                 :height "60%")
        {initialize3! :initialize!
         build-pad-textfieldx :textfieldx} (textfieldx :id "fn-pad2"
                                                       :height "25%")
        {initialize4! :initialize!
         build-docstring :textfieldx} (textfieldx :id "build-docstring"
                                                  :height "25%")
        *current-f* (atom nil)
        made-screen
          (build-screen nifty
            (screen
             :controller (proxy [DefaultScreenController] []
                           (onStartScreen [] 
                              (clean-all! (.getCurrentScreen nifty) nifty)
                              (initialize! nifty (.getCurrentScreen nifty))
                              (initialize2! nifty)
                              (initialize3! nifty)
                              (initialize4! nifty)
                                          ))
              :layers
              [
              (layer
                :align :left
                :child-layout :horizontal
                :id "fn-layer"
                :panels
                [(panel :id "alert-panel"
                        :child-layout :center
                        :style "nifty-panel-bright"
                        :valign :top
                        :width "*"
                        :visible? false
                        :visible-to-mouse? true
                        :control (label :valign :top
                                        :align :left
                                        :padding "0%"
                                        :wrap? true
                                        :color "#000"
                                        :width "100%"
                                        :id "alert-text")
                        :on-click-effect (effect :effect-name "hide"
                                                 :effect-parameters
                                                 {"targetElement" "alert-panel"})
                        :on-hide-effect (effect :effect-name "move"
                                                :inherit? true
                                                :effect-parameters
                                                {"mode" "out"
                                                 "direction" "top"}
                                                :length 500
                                                :start-delay 0)
                        :on-show-effect (effect :effect-name "move"
                                                :inherit? true
                                                :effect-parameters
                                                {"mode" "in"
                                                 "direction" "top"}
                                                :length 500
                                                :start-delay 0))

                 (panel :id "fn-build-pad"
                        :visible? false
                        :on-show-effect
                          (effect :effect-name "move"
                                  :inherit? true
                                  :effect-parameters
                                  {"mode" "in"
                                   "direction" "top"}
                                  :length 1000
                                  :start-delay 0)
                        :on-hide-effect
                          (effect :effect-name "move"
                                  :inherit? true
                                  :effect-parameters
                                  {"mode" "out"
                                   "direction" "top"}
                                  :length 1000
                                  :start-delay 0)
                        :child-layout :vertical
                        :height "100%"
                        :width "33%"
                        :align :right
                        :controls
                        [(label :text "Module Name"
                                :color "#000")
                         (drop-down :id "fn-build-modules"
                                    :width "100%")
                         (label :text "Function Name"
                                :color "#000")
                         (text-field :id "fn-build-name"
                                 :width "100%")
                         (label :text "Parameters"
                                :color "#000")
                         (text-field :id "fn-build-params"
                                     :width "100%")
                         (label :text "Code"
                                :color "#000")
                         build-pad-textfieldx
                         (label :text "Docs"
                                :color "#000")
                         build-docstring
                         ]
                        :panel
                         (panel :child-layout :horizontal
                                :controls
                                [(button :label "Save"
                                         :id "fn-build-save")
                                 (button :label "Close"
                                         :id "fn-build-close")]))
                (panel
                  :on-show-effect
                    (effect :effect-name "move"
                            :inherit? true
                            :effect-parameters {"mode" "in"
                                                "direction" "right"})
                  :on-hide-effect
                    (effect :effect-name "move"
                            :inherit? true
                            :effect-parameters {"mode" "out"
                                                "direction" "right"})
                  :id "fn-panels"
                  :visible? false
                  :child-layout :vertical
                  :height "100%"
                  :width "33%"
                  :align :right
                  :panels
                  [(panel :child-layout :vertical
                          :height "25%"
                          :controls [(label :id "fn-pad-param"
                                            :width "100%"
                                            :text ""
                                            :color "#000")
                                     pad-textfieldx]
                          )
                   (panel
                     :height "7%"
                     :child-layout :center
                     :align :left
                     :control
                        (button :label "Send"
                                  :id "compute"))
                   (panel
                     :height "7%"
                     :child-layout :horizontal
                     :align :left
                     :controls
                       [(button :label "Clear"
                                :id "clear")
                        (button :label "Save"
                                :id "save")])
                   (panel :id "fn-controls"
                          :child-layout :vertical
                          :height "45%"
                          :align :right 
                          :panels [(doto fn-box (.align (keyword->align :left))
                                                (.height "100%"))])
                   (panel :height "10%"
                          :child-layout :horizontal
                          :align :center
                          :controls
                          [(button :label "New" :id "fn-new"
                                   :width "33%")
                           (button :label "Edit" :id "fn-edit"
                                   :width "33%")
                           (button :label "Delete" :id "fn-delete"
                                   :width "33%")])
                   (panel :height "10%"
                          :child-layout :vertical
                          :valign :bottom
                          :control
                          (button :label "Close" :id "main-close"))
                   ])])
               ]))
        my-alert! (partial alert! made-screen)
        set-bench! (fn [module fn-name fn-map]
                     (-> (select made-screen "fn-build-modules")
                         (nifty-control :drop-down)
                         (.selectItem module))
                     (-> (select made-screen "fn-build-name")
                         (nifty-control :text-field)
                         (.setText (if (= fn-name " ") ""
                                       fn-name)))
                     (if (not= "" fn-name)
                       (let [f (get-in fn-map [module fn-name])]
                         (-> (select made-screen "fn-build-params")
                             (nifty-control :text-field)
                             (.setText (str/join " " (map name*
                                                          (parameters f)))))
                             (when (implements? Sourceable f)
                             (set-lines! nifty
                                         (select made-screen "fn-pad2")
                                         (or (source-code f) "")))
                             (set-lines! nifty
                                         (select made-screen "build-docstring")
                                          (docstring f))
                             

                         )
                       (do
                         (-> (select made-screen "fn-build-params")
                             (nifty-control :text-field)
                             (.setText ""))
                             (set-lines! nifty (select made-screen "fn-pad2")
                                               "")
                             (set-lines! nifty (select made-screen "build-docstring")
                                               "")


                         )))

                             
        ]
    (apply-interactions made-screen
      :main-close {:on-left-click
                  #(doto (select made-screen "fn-panels")
                     (.setVisible false))}
      :fn-new {:on-left-click 
                 #(let [build-pad (select made-screen "fn-build-pad")]
                    (.setVisible build-pad true)
                    (set-bench! "User" "" @*fn-map*))}
      :fn-edit {:on-left-click
                #(when-let [[module fn-name] (modfn made-screen)]
                   (if (not (is-core-fn? module fn-name))
                     (do 
                       (.setVisible (select made-screen "fn-build-pad")
                                    true)
                       (set-bench! module fn-name @*fn-map*))))}
      :fn-delete {:on-left-click
                  #(when-let [[module fn-name] (modfn made-screen)]
                     (if (not (is-core-fn? module fn-name))
                       (do
                         (swap! *fn-map* (fn [m] 
                                           (assoc m module
                                              (dissoc (m module)
                                                      fn-name))))
                         (save-user-functions! @*fn-map*)
                         (invalidate! made-screen module fn-name))
                       (my-alert! (Exception. "Cannot delete a core function."))))}
      :fn-build-close {:on-left-click
                  #(let [build-pad (select made-screen "fn-build-pad")]
                    (.setVisible build-pad false))}
      :fn-build-save {:on-left-click
                      #(try
                        (let [build-pad (select made-screen "fn-build-pad")
                             module (-> (select made-screen "fn-build-modules")
                                        (nifty-control :drop-down)
                                        (.getSelection))
                             fn-name (-> (select made-screen "fn-build-name")
                                         (nifty-control :text-field)
                                         (.getText))
                             param-text (str/trim
                                          (text-field-text
                                            (select build-pad "fn-build-params")))
                             params (if (= "" param-text) []
                                      (str/split param-text #"\s+"))
                             code (lines (first-entry 
                                           (select build-pad
                                                   "fn-pad2")))
                             docstring (lines (first-entry
                                                (select build-pad
                                                        "build-docstring")))
                             ]
                         (cond
                            (not (is-var-name? fn-name))
                           (my-alert! "Function name is invalid.")
                            (is-core-fn? module fn-name)
                           (my-alert! "Can't override a core-function.")
                            (not (every? is-var-name? params))
                           (my-alert! "Invalid parameter name.")
                            (not= (count params) (count (distinct params)))
                           (my-alert! "Parameter names must be unique.")
                           :else
                           (let [pog-fn (code-pog-fn params docstring
                                          code)]
                             (swap! *fn-map* assoc-in [module fn-name]
                                             pog-fn)
                             (save-user-functions! @*fn-map*)
                             (invalidate! made-screen module fn-name true))))
                         (catch Exception e
                           (my-alert! (str 
                                           (.getMessage e)))
                           ))}
      :save {:on-left-click (fn []
                              (set-bench! "User" " "
                                (update-in @*fn-map* ["User"]
                                  assoc " "
                                  (reify 
                                    Sourceable
                                    (source-code [_] 
                                      (-> (select made-screen "fn-pad")
                                          (first-entry)
                                          (lines)))
                                    PogFn
                                    (docstring [_] "")
                                    (parameters [_] []))
                                    ))
                              (.setVisible (select made-screen "fn-build-pad")
                                           true))}
      :clear {:on-left-click #(set-lines! nifty
                                          (select made-screen "fn-pad")
                                          "")}
      :compute {:on-left-click #(try
                                  (let [pog-fn (get-pog-fn (select made-screen "fn-pad")
                                                  @*current-f*
                                                  @*fn-map*)
                                        new-env (into {}
                                                  (for [[module fs] @*fn-map*
                                                     f fs]
                                                    f))]
                                    (reset! internal-core-env new-env)
                                    (invoke* pog-fn new-env []))
                                  (catch Exception e
                                    (my-alert! e)))}
                        )
    {:set-current-function! 
       (fn [pog-fn name]
         (let [f (partial* pog-fn {(name* (first (parameters pog-fn)))
                                   my-alert!})]
         (swap! *current-f* (constantly f))
         (set-pog-fn! (select made-screen "fn-pad-param")
                      nifty f name)
         (.setVisible (select made-screen "fn-panels") true)))
     :initialize! (partial load-user-functions! *fn-map*)
     :function-screen made-screen
     :alert! my-alert!}))
