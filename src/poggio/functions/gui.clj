(ns poggio.functions.gui
  (:import [de.lessvoid.nifty.screen DefaultScreenController])
  (:require [clojure.string :as str]
            [nifty-clj [builders :as builders]]) 
  (:use [data coll object]
        [nifty-clj [builders :exclude [text]] elements events popup textfieldx]
        [poggio.functions core parser modules]))

(defn set-pog-fn! [fn-pad-param nifty f]
  (set-text! fn-pad-param (name* (first (parameters f)))))

(defn get-pog-fn [fn-pad f fn-map ]
  (seq->pog-fn "" [] 
    (list f (code-pog-fn [] "" (lines (first-entry fn-pad))))))

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
                            (implements? Sourceable f) (or (source-code f) "")
                            :else "Source code is unavailable."))

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
        fn-map (into {} modules)]
  {:fn-map fn-map
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
                                       (sort (map first (fn-map module)))))))))

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
                              f ((fn-map module) fn-name)
                              info-pane (select screen "pog-fns-info")]
                         (update! (first (.getElements info-pane)) f))))))

                  (subscribe! nifty "pog-fns-search" :text-field-change
                    (fn [topic data]
                      (let [text (str/lower-case (.getText data))
                            matches (for [[module functions] fn-map
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
  (let [{:keys[fn-map fn-box initialize! clean!
               selected-fn!]} (apply function-box modules)
        {initialize2! :initialize!
         textfieldx :textfieldx} (textfieldx :id "fn-pad")
        *fn-map* (atom fn-map)
        *current-f* (atom nil)
        made-screen
          (build-screen nifty
            (screen
             :controller (proxy [DefaultScreenController] []
                           (onStartScreen [] 
                              (initialize! nifty (.getCurrentScreen nifty))
                              (initialize2! nifty)))
              :layer
              (layer
                :align :right
                :child-layout :center
                :id "fn-layer"
                :panel
                (panel
                  :id "fn-panels"
                  :child-layout :vertical
                  :height "100%"
                  :width "40%"
                  :align :right
                  :panels
                  [(panel :child-layout :vertical
                          :height "30%"
                          :controls [(label :id "fn-pad-param"
                                            :width "100%"
                                            :text ""
                                            :color "#000")
                                     textfieldx]
                          :padding "2px")
                   (panel :id "fn-controls"
                          :child-layout :vertical
                          :height "60%"
                          :align :right 
                          :panels [(doto fn-box (.align (keyword->align :left))
                                                (.height "100%"))])
                   (panel
                     :height "10%"
                     :child-layout :center
                     :align :left
                     :control
                        (button :label "Send"
                                  :id "compute"))]))))]
    (apply-interactions made-screen
      :compute {:on-left-click #(-> (get-pog-fn (select made-screen "fn-pad")
                                                  @*current-f*
                                                  @*fn-map*)
                                     (invoke* , (into {}
                                                 (for [[module, fs] @*fn-map*
                                                       f fs]
                                                   f))
                                              []))})
    {:set-current-function! 
       (fn [pog-fn]
         (swap! *current-f* (constantly pog-fn))
         (set-pog-fn! (select made-screen "fn-pad-param")
                      nifty pog-fn))
     :function-screen made-screen}))
