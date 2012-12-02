(ns nifty-clj.builders
  "Functions for creating nifty objects."
  (:use [data.function :only [no-op]]
        [control.defutilities :only [def-opts-constructor]]
        [jme-clj assets audio input view-port])
  (:import [com.jme3.app Application] 
           [com.jme3.asset AssetManager]
           [com.jme3.audio AudioRenderer]
           [com.jme3.input InputManager]
           [com.jme3.niftygui NiftyJmeDisplay]
           [com.jme3.renderer ViewPort]
           [de.lessvoid.nifty Nifty NiftyMethodInvoker]
           [de.lessvoid.nifty.builder EffectBuilder
                                      ElementBuilder 
                                      ElementBuilder$Align
                                      ElementBuilder$ChildLayoutType
                                      ElementBuilder$VAlign
                                      HoverEffectBuilder
                                      ScreenBuilder
                                      ImageBuilder
                                      LayerBuilder
                                      PanelBuilder
                                      TextBuilder]
           [de.lessvoid.nifty.effects Falloff$HoverFalloffConstraint
                                      Falloff$HoverFalloffType]
           [de.lessvoid.nifty.controls.button.builder ButtonBuilder]
           [de.lessvoid.nifty.controls.checkbox.builder CheckboxBuilder]
           [de.lessvoid.nifty.controls.label.builder LabelBuilder]
           [de.lessvoid.nifty.controls.scrollpanel.builder ScrollPanelBuilder]
           [de.lessvoid.nifty.controls.textfield.builder TextFieldBuilder]
           [de.lessvoid.nifty.screen DefaultScreenController
                                     ScreenController]))

(extend-type Application
  NiftyJmeDisplayer
  (asset-manager [app] (.getAssetManager app))
  (input-manager [app] (.getInputManager app))
  (audio-renderer [app] (.getAudioRenderer app))
  (view-port [app] (.getGuiViewPort app)))

(defn nifty-jme-display
  "Returns a new NiftyJmeDisplay object using the given
   arguments. You can instead send an object that implements
   the AssetManagerProvider,InputManagerProvider,AudioRenderProvider, and
   ViewPortProvider protocols to provide the arguments."
  ([displayer]
   (nifty-jme-display (asset-manager displayer)
                      (input-manager displayer)
                      (audio-renderer displayer)
                      (view-port displayer)))
  ([^AssetManager asset-manager ^InputManager input-manager
    ^AudioRenderer audio-renderer ^ViewPort view-port]
   (NiftyJmeDisplay. asset-manager
                     input-manager
                     audio-renderer
                     view-port)))

(defn nifty
  "Returns a Nifty instance from a newly created NiftyJmeDisplay
   that is automatically added to its viewport. See nifty-jme-display
   for details about parameters."
  ([displayer]
   (nifty displayer true))
  ([displayer load-defaults?]
   (nifty (asset-manager displayer)
          (input-manager displayer)
          (audio-renderer displayer)
          (view-port displayer)
          load-defaults?))
  ([asset-manager input-manager audio-renderer view-port]
   (nifty asset-manager input-manager audio-renderer view-port true))
  ([asset-manager input-manager audio-renderer view-port load-defaults?]
   (let [display (nifty-jme-display asset-manager
                                    input-manager
                                    audio-renderer
                                    view-port)]
     (.addProcessor view-port display)
     (let [display-nifty (.getNifty display)]
       (when load-defaults?
         (.loadStyleFile display-nifty "nifty-default-styles.xml")
         (.loadControlFile display-nifty "nifty-default-controls.xml"))
       display-nifty))))
      

(def-opts-constructor screen
  [:simple]
  {:id "screen-builder generated ScreenBuilder"}
  `(ScreenBuilder. ~'id)
  {:id     [:no-op]
   :layers [:do-seq
              :replace [#"s$" ""]]})

(def keyword->child-layout
  {:absolute        ElementBuilder$ChildLayoutType/Absolute
   :absolute-inside ElementBuilder$ChildLayoutType/AbsoluteInside
   :center          ElementBuilder$ChildLayoutType/Center
   :horizontal      ElementBuilder$ChildLayoutType/Horizontal
   :overlay         ElementBuilder$ChildLayoutType/Overlay
   :vertical        ElementBuilder$ChildLayoutType/Vertical})

(def keyword->align
  {:center ElementBuilder$Align/Center
   :left   ElementBuilder$Align/Left
   :right  ElementBuilder$Align/Right})

(def keyword->valign
  {:bottom ElementBuilder$VAlign/Bottom
   :center ElementBuilder$VAlign/Center
   :top    ElementBuilder$VAlign/Top})

(def element-builder-handlers
  {:align             [:simple
                         :thread-in `(keyword->align)]
   :child-layout      [:simple
                         :thread-in `(keyword->child-layout)]
   :child-clip?       [:simple
                         :replace [#"\?$" ""]]
   :controls          [:do-seq
                         :replace [#"s$" ""]]
   :focusable?        [:simple
                         :replace [#"\?$" ""]]
   :images            [:do-seq
                         :replace [#"s$" ""]]
   :panels            [:do-seq
                         :replace [#"s$" ""]]
   :text-halign       [:simple
                         :thread-in `(keyword->align)]
   :text-valign       [:simple
                         :thread-in `(keyword->valign)]
   :valign            [:simple
                         :thread-in `(keyword->valign)]
   :visible?          [:simple
                         :replace [#"\?$" ""]]
   :visible-to-mouse? [:simple
                         :replace [#"\?$" ""]]})

(defmacro def-element-builder 
  "This macro creates a function similar to 
   def-opts-constructor, except it merges the handlers
   with element-builder-handlers."
  [name defaults constructor handlers]
  `(def-opts-constructor ~name 
      [:simple]
      ~defaults
      ~constructor
      (merge element-builder-handlers ~handlers)))

(def-element-builder layer
  {:id "layer-generated LayerBuilder"}
  `(LayerBuilder. ~'id)
  {:id [:no-op]})

(def-element-builder panel
  {:id "panel-generated PanelBuilder"}
  `(PanelBuilder. ~'id)
  {:id [:no-op]})

(def-element-builder image
  {:id "image-generated ImageBuilder"}
  `(ImageBuilder. ~'id)
  {:id [:no-op]})

(def-element-builder text
  {:id "text-generated TextBuilder"}
  `(TextBuilder. ~'id)
  {:id    [:no-op]
   :wrap? [:simple
             :replace [#"\?$" ""]]})

(def-element-builder button
  {:id    "button-generated ButtonBuilder"
   :label "Click here"}
  `(ButtonBuilder. ~'id ~'label)
  {:id    [:no-op]
   :label [:no-op]})

(def-element-builder label
  {:id "label-generated LabelBuilder"
   :text "Label"}
  `(LabelBuilder. ~'id)
  {:id [:no-op]})

(def-element-builder checkbox
  {:id "checkbox-generated CheckboxBuilder"}
  `(CheckboxBuilder. ~'id)
  {:id [:no-op]
   :checked? [:simple
                :replace [#"\?$" ""]]})

(def-element-builder scroll-panel
  {:id "scroll-panel-generated ScrollPanelBuilder"}
  `(ScrollPanelBuilder. ~'id)
  {:id [:no-op]})
   
(def-element-builder text-field
  {:id "text-field-generated TextFieldBuilder"}
  `(TextFieldBuilder. ~'id)
  {:id [:no-op]})

(def-opts-constructor effect
  [:simple]
  {:effect-name "fade"}
  `(EffectBuilder. ~'effect-name)
  {:effect-name [:no-op]
   :effect-parameters [:map-do-seq
                         :replace [#"s$" ""]]
   :effect-value [:simple
                    :append-in `(into-array String)]
   :inherit?     [:simple
                    :replace [#"\?$" ""]]
   :never-stop-rendering? [:simple
                             :replace [#"\?$" ""]]
   :on-shot? [:simple
                :replace [#"\?$" ""]]
   :overlay? [:simple
                :replace [#"\?$" ""]]
   :post?    [:simple
                :replace [#"\?$" ""]]})

(def keyword->hover-falloff-constraint
  {:both       Falloff$HoverFalloffConstraint/both
   :horizontal Falloff$HoverFalloffConstraint/horizontal
   :none       Falloff$HoverFalloffConstraint/none
   :vertical   Falloff$HoverFalloffConstraint/vertical})

(def keyword->hover-falloff-type
  {:linear     Falloff$HoverFalloffType/linear
   :none       Falloff$HoverFalloffType/none})

(def-opts-constructor hover-effect
  [:simple]
  {:effect-name "fade"}
  `(HoverEffectBuilder. ~'effect-name)
  {:effect-name [:no-op]
   :effect-parameters [:map-do-seq
                         :replace [#"s$" ""]]
   :hover-parameters  [:map-do-seq
                         :replace [#"s$" ""]]
   :inherit?          [:simple
                         :replace [#"\?$" ""]]
   :never-stop-rendering? [:simple
                             :replace [#"\?$" ""]]
   :overlay?              [:simple
                             :replace [#"\?$" ""]]
   :post?                 [:simple
                             :replace [#"\?$" ""]]})
