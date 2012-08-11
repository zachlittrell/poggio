(ns nifty-clj.builders
  (:use [functions.utilities :only [no-op]]
        [control.defutilities :only [def-opts-constructor
                                     keyword->adder-defn-form
                                     keyword->adder-symbol
                                     keyword->setter-defn-form
                                     predicate-keyword->setter-symbol
                                     keyword->setter-symbol
                                     def-directive-map
                                     default-director
                                     default-directives-map]])
  (:import [de.lessvoid.nifty Nifty]
           [de.lessvoid.nifty.builder EffectBuilder
                                      ElementBuilder 
                                      ElementBuilder$Align
                                      ElementBuilder$ChildLayoutType
                                      ElementBuilder$VAlign
                                      ScreenBuilder
                                      ImageBuilder
                                      LayerBuilder
                                      PanelBuilder
                                      TextBuilder]
           [de.lessvoid.nifty.controls.button.builder ButtonBuilder]
           [de.lessvoid.nifty.screen DefaultScreenController
                                     ScreenController]))

(def-opts-constructor screen
  {:id "screen-builder generated ScreenBuilder"
   :controller (DefaultScreenController.)}
  (ScreenBuilder. id controller)
  {:id no-op
   :controller no-op
   :layers (fn [^ScreenBuilder screen-build layers]
             (doseq [layer layers]
               (.layer screen-build layer)))})

(def child-layout-keyword->child-layout
  {:absolute       ElementBuilder$ChildLayoutType/Absolute
   :asolute-inside ElementBuilder$ChildLayoutType/AbsoluteInside
   :center         ElementBuilder$ChildLayoutType/Center
   :horizontal     ElementBuilder$ChildLayoutType/Horizontal
   :overlay        ElementBuilder$ChildLayoutType/Overlay
   :vertical       ElementBuilder$ChildLayoutType/Vertical})

(def align-keyword->align
  {:center ElementBuilder$Align/Center
   :left   ElementBuilder$Align/Left
   :right  ElementBuilder$Align/Right})

(def valign-keyword->valign
  {:bottom ElementBuilder$VAlign/Bottom
   :center ElementBuilder$VAlign/Center
   :top    ElementBuilder$VAlign/Top})

(def keyword->align-setter-defn-form
  (partial keyword->setter-defn-form
           (partial list `align-keyword->align)))

(def keyword->child-layout-setter-defn-form
  (partial keyword->setter-defn-form
           (partial list `child-layout-keyword->child-layout)))

(def keyword->valign-setter-defn-form
  (partial keyword->setter-defn-form
           (partial list `valign-keyword->valign)))

(def-directive-map element-builder-directives-map
  (let [setter-map (:setter default-directives-map)]
    (assoc default-directives-map
           :align-setter 
              (assoc setter-map
                     :form keyword->align-setter-defn-form)
           :child-layout-setter
              (assoc setter-map
                     :form keyword->child-layout-setter-defn-form)
           :valign-setter
              (assoc setter-map
                     :form keyword->valign-setter-defn-form)
           :predicate-setter
              (assoc setter-map
                     :name predicate-keyword->setter-symbol)))
    default-director)

(def element-builder-handlers
  (element-builder-directives-map 
    [:align                  :align-setter]
    [:background-color       :setter]
    [:background-image       :setter]
    [:child-layout           :child-layout-setter]
    [:child-clip?            :predicate-setter]
    [:color                  :setter]
    [:controls               :adder]
    [:controller             :setter]
    [:filename               :setter]
    [:focusable?             :predicate-setter]
    [:font                   :setter]
    [:height                 :setter]
    [:images                 :adder]
    [:image-mode             :setter]
    [:input-mapping          :setter]
    [:inset                  :setter]
    [:name                   :setter]
    [:on-active-effect       :setter]
    [:on-click-effect        :setter]
    [:on-custom-effect       :setter]
    [:on-end-hover-effect    :setter]
    [:on-end-screen-effect   :setter]
    [:on-focus-effect        :setter]
    [:on-get-focus-effect    :setter]
    [:on-hide-effect         :setter]
    [:on-hover-effect        :setter]
    [:on-lost-focus-effect   :setter]
    [:on-show-effect         :setter]
    [:on-start-hover-effect  :setter]
    [:on-start-screen-effect :setter]
    [:padding                :setter]
    [:padding-bottom         :setter]
    [:padding-left           :setter]
    [:padding-right          :setter]
    [:padding-top            :setter]
    [:panels                 :adder]
    [:selection-color        :setter]
    [:style                  :setter]
    [:text                   :setter]
    [:text-halign            :align-setter]
    [:text-valign            :valign-setter]
    [:valign                 :valign-setter]
    [:visible?               :predicate-setter]
    [:visible-to-mouse?      :predicate-setter]
    [:width                  :setter]
    [:x                      :setter]
    [:y                      :setter]))

(defmacro def-element-builder 
  "This macro creates a function similar to 
   def-opts-constructor, except it merges the handlers
   with element-builder-handlers."
  [name defaults constructor handlers]
  `(def-opts-constructor ~name ~defaults ~constructor
                         (merge element-builder-handlers ~handlers)))

(def-element-builder layer
  {:id "layer-generated LayerBuilder"}
  (LayerBuilder. id)
  {:id no-op})

(def-element-builder panel
  {:id "panel-generated PanelBuilder"}
  (PanelBuilder. id)
  {:id no-op})

(def-element-builder image
  {:id "image-generated ImageBuilder"}
  (ImageBuilder. id)
  {:id no-op})

(def-element-builder text
  {:id "text-generated TextBuilder"}
  (TextBuilder. id)
  {:id    no-op
   :wrap? (fn [^TextBuilder text-build wrap?]
            (.wrap text-build wrap?))})

(def-element-builder button
  {:id    "button-generated ButtonBuilder"
   :label "Click here"}
  (ButtonBuilder. id label)
  {:id    no-op
   :label no-op})
