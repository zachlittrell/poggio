(ns poggio.functions.gui
  (:import [de.lessvoid.nifty.screen DefaultScreenController])
  (:require [nifty-clj [builders :as builders]]) 
  (:use [data coll]
        [nifty-clj [builders :exclude [text]] elements events]
        [poggio.functions core modules]))


(defn param->button
  ([id param]
   (param->button id param false))
  ([id param headless?]
   (let [head (button :id (name* param)
                      :label (name* param))]
     (when headless?
       (.visible head false)
       (.width head "0px"))
     {:id (name* param)
      :button (panel :id id
                :child-layout :horizontal
                :align :left
                :valign :center
                :control head)})))

(defn get-pog-fn! [this fn-map]
  (let [[head & params] (.getElements this)
        f (look-up-path fn-map (.getId head))]
    (partial* f (into {} (for [[elem param] (zip params (parameters f))
                               :let [[head* & params*] (.getElements elem)]
                               :when (not= (.getId head*) (name* param))]
                           [(name* param) (get-pog-fn! elem fn-map)])))))


(defn set-pog-fn! [this nifty path fn-map click-handler]
 (let [[head & params] (.getElements this)]
   ;;Remove old parameters
   (doseq [child params]
     (.markForRemoval child))
   (if path
     (let [f (look-up-path fn-map path)]
       ;;Set the id to be the path
       ;;So when we try to reconstruct this function,
       ;;we know what to look-up.
       (doto head
        (.setId path)
        (set-button-text! (second (path-components path))))
       ;;Add parameters
       (doseq [param (parameters f)]
         (let [{:keys [id button]} (param->button
                                                 " "
                                                 param)
               made-button (build nifty this button)]
           (apply-interactions made-button
              id {:on-left-click (partial click-handler
                                          (select this " "))})
           (.setId made-button "")))
      ;; (.layoutLayers (.getCurrentScreen nifty))
      )
      (let [parent (.getParent this)
            [head* & params*] (.getElements parent)
            parent-f (look-up-path fn-map (.getId head*))
            param (nth (parameters parent-f) (.indexOf params* this))]
        (doto head
          (.setId (name* param))
          (set-button-text! (name* param)))
        )
   )))


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
                                                  :width "100%"
                                                  :height "100%"
                                                  :text-h-align :left
                                                  :text-v-align :top
                                                  :wrap? true)))])
   :update! (fn [info f]
                  (let [scroll (select info "#nifty-scrollpanel-child-root")]
                    (set-text! (select info "doc-label") 
                               (if f (docstring f)
                                     ""))
                    (.layoutElements scroll)
                    (.setUp (nifty-control info :scroll) 0 20 0 200 de.lessvoid.nifty.controls.ScrollPanel$AutoScroll/OFF)))})

(defn function-box [& modules]
  (let [{:keys [update! info]} (function-info)
        box (panel :child-layout :vertical
                   :padding "1px"
                   :width "100%"
                   :panels 
                   [(panel :child-layout :vertical
                           :controls 
                             [(drop-down :id "pog-mods-drop"
                                         :width "100%")
                              (drop-down :id "pog-fns-drop"
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
                     (when (and (> (.getSelectedIndex modules) 0)
                                (> (.getSelectedIndex functions) 0))
                       (path (.getSelection modules)
                             (.getSelection functions)))))
                           

   :initialize! (fn [nifty screen]
                  (let [modules (-> screen
                                     (select , "pog-mods-drop")
                                     (nifty-control :drop-down))]
                   (.addItem modules "Modules")
                   (.addAllItems modules
                                 (sort (keys fn-map))))
                  (subscribe! nifty "pog-mods-drop" :drop-down-select
                    (fn [topic data]
                      (if (<= (.getSelectionItemIndex data) 0)
                        (do (.clear (-> screen (select , "pog-fns-drop")
                                               (nifty-control :drop-down))))
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
                        (let [name (.getSelection data)
                              f ((fn-map (-> screen
                                            (select , "pog-mods-drop")
                                            (nifty-control :drop-down)
                                            (.getSelection)))
                                            name)
                              info-pane (select screen "pog-fns-info")]
                         (update! (first (.getElements info-pane)) f)))))))
   :clean!       (fn [])}))


(defn function-screen [nifty & modules]
  (let [{:keys[fn-map fn-box initialize! clean!
               selected-fn!]} (apply function-box modules)
        {fn-pad :button
         id :id} (param->button "fn-pad" "fn-pad-fn" true)
        *fn-map* (atom fn-map)
        *focused-param* (atom nil)
        made-screen
          (build-screen nifty
            (screen
             :controller (proxy [DefaultScreenController] []
                           (onStartScreen [] 
                              (initialize! nifty (.getCurrentScreen nifty)))) 
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
                          :height "10%"
                          :control (scroll-panel :width "100%"
                                                 :panel fn-pad
                                                 :set {"vertical" "false"}
                                                 :id "fn-pad-scroller"
                                                 :style "nifty-listbox"))
                   (panel :id "fn-controls"
                          :visible? false
                          :child-layout :vertical
                          :height "80%"
                          :align :right 
                          :panels [(panel :child-layout :center
                                          :control (label 
                                                     :color "#000"
                                                     :text
                                                      "Use Selected Function?"))
                                   (panel :child-layout :horizontal
                                          :controls
                                           [(button :label "Ok"
                                                    :width "33%"
                                                    :id "ok")
                                            (button :label "Clear"
                                                    :width "33%"
                                                    :id "clear")
                                            (button :label "Cancel"
                                                    :width "33%"
                                                    :id "cancel")])
                                   (doto fn-box (.align (keyword->align :left))
                                                (.height "60%"))])
                   (panel
                     :height "10%"
                     :child-layout :center
                     :align :left
                     :control
                        (button :label "Compute"
                             :id "compute"))]))))
        param-click (fn [element]
                      (.setVisible (select made-screen "fn-controls") true)
                      (swap! *focused-param* (constantly element)))]
    (apply-interactions made-screen
      :ok {:on-left-click (fn []
                            (when-let [selected-fn (selected-fn! nifty made-screen)]
                              (set-pog-fn! @*focused-param*
                                          nifty
                                          selected-fn
                                          @*fn-map*
                                          param-click)
                              (.setVisible (select made-screen "fn-controls") false)))}
      :cancel {:on-left-click (fn []
                                (.setVisible (select made-screen "fn-controls")
                                             false))}
      :clear {:on-left-click (fn []
                               (set-pog-fn! @*focused-param*
                                            nifty
                                            nil
                                            @*fn-map*
                                            param-click)
                               (.setVisible (select made-screen "fn-controls") false))}
      :compute {:on-left-click #(-> (get-pog-fn! (select made-screen "fn-pad")
                                                  @*fn-map*)
                                     (invoke , []))})
    {:set-current-function! 
       (fn [pog-fn]
         (set-pog-fn! (select made-screen "fn-pad")
                      nifty (path "," ",")
                      (swap! *fn-map* assoc "," {"," pog-fn})
                      param-click))
     :function-screen made-screen}))
