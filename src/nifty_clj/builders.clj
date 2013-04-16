(ns nifty-clj.builders
  "Functions for creating nifty objects."
  (:use [data [function :only [no-op]]
              [keyword  :only [keyword->symbol]]
              [string   :only [dash->camel-case]]]
        [control.bindings :only [let-weave]]
        [control.defutilities :only [def-opts-constructor]]
        [control.io :only [err-println]]
        [jme-clj assets audio input view-port]
        [nifty-clj events])
  (:import [com.jme3.app Application] 
           [com.jme3.asset AssetManager]
           [com.jme3.audio AudioRenderer]
           [com.jme3.input InputManager]
           [com.jme3.niftygui NiftyJmeDisplay]
           [com.jme3.renderer ViewPort]
           [de.lessvoid.nifty Nifty NiftyMethodInvoker]
           [de.lessvoid.nifty.builder ControlBuilder
                                      EffectBuilder
                                      ElementBuilder 
                                      ElementBuilder$Align
                                      ElementBuilder$ChildLayoutType
                                      ElementBuilder$VAlign
                                      HoverEffectBuilder
                                      ScreenBuilder
                                      ImageBuilder
                                      LayerBuilder
                                      PanelBuilder
                                      PopupBuilder
                                      TextBuilder]
           [de.lessvoid.nifty.effects Falloff$HoverFalloffConstraint
                                      Falloff$HoverFalloffType]
           [de.lessvoid.nifty.elements Element]
           [de.lessvoid.nifty.controls.button.builder ButtonBuilder]
           [de.lessvoid.nifty.controls.checkbox.builder CheckboxBuilder]
           [de.lessvoid.nifty.controls.dragndrop.builder DraggableBuilder
                                                         DroppableBuilder]
           [de.lessvoid.nifty.controls.label.builder LabelBuilder]
           [de.lessvoid.nifty.controls.scrollpanel.builder ScrollPanelBuilder]
           [de.lessvoid.nifty.controls.textfield.builder TextFieldBuilder]
           [de.lessvoid.nifty.loaderv2.types ElementType]
           [de.lessvoid.nifty.screen Screen
                                     DefaultScreenController
                                     ScreenController]
           [java.util.logging Level Logger]))

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
     (let [display-nifty (.getNifty display)]
     (.addProcessor view-port display)
       (.setLevel (Logger/getLogger "de.lessvoid.nifty") Level/SEVERE)
       (.setLevel (Logger/getLogger "NiftyInputEventHandlingLog") Level/SEVERE)
       (when load-defaults?
         (.loadStyleFile display-nifty "nifty-default-styles.xml")
         (.loadControlFile display-nifty "nifty-default-controls.xml"))
       [display display-nifty]))))
      

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
  {:_element-interactions [:no-op]
   :align             [:simple
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
  [name defaults element-class args handlers]
   `(def-opts-constructor ~name 
      [:simple]
      ~defaults
      `(new ~~element-class ~@'~args)
      (merge element-builder-handlers ~handlers)))

(def-element-builder control
  {:id "control-generated ControlBuilder"
   :name "label"}
  ControlBuilder [id name]
  {:id [:no-op]
   :name [:no-op]})

(def-element-builder layer
  {:id "layer-generated LayerBuilder"}
  LayerBuilder [id]
  {:id [:no-op]})

(def-element-builder panel
  {:id "panel-generated PanelBuilder"}
  PanelBuilder [id]
  {:id [:no-op]})

(def-element-builder image
  {:id "image-generated ImageBuilder"}
  ImageBuilder [id]
  {:id [:no-op]})

(def-element-builder text
  {:id "text-generated TextBuilder"}
  TextBuilder [id]
  {:id    [:no-op]
   :wrap? [:simple
             :replace [#"\?$" ""]]})

(def-element-builder button
  {:id    "button-generated ButtonBuilder"
   :label "Click here"}
  ButtonBuilder [id label]
  {:id    [:no-op]
   :label [:no-op]})

(def-element-builder label
  {:id "label-generated LabelBuilder"
   :text "Label"}
  LabelBuilder [id]
  {:id [:no-op]})

(def-element-builder checkbox
  {:id "checkbox-generated CheckboxBuilder"}
  CheckboxBuilder [id]
  {:id [:no-op]
   :checked? [:simple
                :replace [#"\?$" ""]]})

(def-element-builder scroll-panel
  {:id "scroll-panel-generated ScrollPanelBuilder"}
  ScrollPanelBuilder [id]
  {:id [:no-op]})
   
(def-element-builder text-field
  {:id "text-field-generated TextFieldBuilder"}
  TextFieldBuilder [id]
  {:id [:no-op]})

(def-element-builder draggable
  {:id "draggable-generated DraggableBuilder"}
  DraggableBuilder [id]
  {:id [:no-op]})

(def-element-builder droppable
  {:id "draggable-generated DroppableBuilder"}
  DroppableBuilder [id]
  {:id [:no-op]})


(def-element-builder popup
  {:id ""}
  PopupBuilder [id]
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
   :one-shot? [:simple
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


(defn laid-out-screen-around [child-layout & controls]
  "Returns a screen with one layer and one panel (which uses the given
   child-layout) that contains the given controls."
  (screen
    :layer
      (layer
        :child-layout :center
        :panel
          (panel
            :child-layout child-layout
            :controls controls))))
        
(defn screen-around [& controls]
  "Returns (apply laid-out-screen-around :center controls)"
  (apply laid-out-screen-around :center controls))

(defn laid-out-screen-around-panels [child-layout & panels]
  "Returns a screen with one layer, which uses the given
   child layout, containing panels."
  (screen
    :layer 
      (layer
        :child-layout child-layout
        :panels panels)))

(defn screen-around-panels [& panels]
  "Returns (apply laid-out-screen-around-panels :center panels)"
  (apply laid-out-screen-around-panels :center panels))

(defn apply-interactions [element & interactions]
  "Returns the element given.
   Can also pass optionally pairs of ids and a map of 
   interaction-handlers. If the id is a vector of an id
   and NiftyControl class keyword, interactions from nifty-control,
   the interaction-handlers mapping keywords 
   for interactions (see nifty-clj.events) to 
   NiftyMethodInvokerProviders, and the keyword being the id of the
   element to apply the interactions to."
  (let [{:as interaction-map} interactions]
    (doseq [[id-key interactions] interaction-map
            [interaction handler] interactions]
      (let-weave (coll? id-key)
         [[id-key control-class] id-key
          target (.findNiftyControl element (name id-key) 
                                    (keyword->nifty-control-class control-class))
          interaction->fn nifty-control-interactions]
         [target (.findElementByName element (name id-key))
          interaction->fn element-interactions]
        (when target
          ((interaction->fn interaction) target handler))))
    element))

(defn build-screen [^Nifty nifty ^ScreenBuilder screen-builder & interactions]
  "Returns the result of applying apply-interactions to the screen built 
   from screen-builder"
  (apply apply-interactions (.build screen-builder nifty) interactions))

(defn build-with-screen [^Nifty nifty ^Screen screen ^Element parent ^ElementBuilder element-builder  & interactions]
  "Returns the result of applying apply-interactions to the element built
   from element-builder."
  (apply apply-interactions (.build element-builder nifty screen parent) interactions))

(defn build [^Nifty nifty ^Element parent ^ElementBuilder element-builder & interactions]
  "Returns (apply build-with-screen nifty (.getCurrentScreen nifty) parent element-builder interactions)."
  (apply build-with-screen nifty (.getCurrentScreen nifty) parent element-builder interactions))
