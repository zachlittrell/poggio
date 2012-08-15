(ns nifty-clj.builders
  (:use [functions.utilities :only [no-op]]
        [control.defutilities :only [def-opts-constructor
                                     keyword->defadder-form
                                     keyword->adder-symbol
                                     keyword->defsetter-form
                                     predicate-keyword->setter-symbol
                                     keyword->setter-symbol
                                     def-directive-map
                                     default-director
                                     default-directives-map]])
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
                                      ScreenBuilder
                                      ImageBuilder
                                      LayerBuilder
                                      PanelBuilder
                                      TextBuilder]
           [de.lessvoid.nifty.controls.button.builder ButtonBuilder]
           [de.lessvoid.nifty.controls.checkbox.builder CheckboxBuilder]
           [de.lessvoid.nifty.controls.label.builder LabelBuilder]
           [de.lessvoid.nifty.controls.scrollpanel.builder ScrollPanelBuilder]
           [de.lessvoid.nifty.controls.textfield.builder TextFieldBuilder]
           [de.lessvoid.nifty.screen DefaultScreenController
                                     ScreenController]))

(defprotocol NiftyJmeDisplayer
  (asset-manager [this])
  (input-manager [this])
  (audio-renderer [this])
  (view-port [this]))

(extend-type Application
  NiftyJmeDisplayer
  (asset-manager [app] (.getAssetManager app))
  (input-manager [app] (.getInputManager app))
  (audio-renderer [app] (.getAudioRenderer app))
  (view-port [app] (.getGuiViewPort app)))

(defn nifty-jme-display
  "Returns a new NiftyJmeDisplay object using the given
   arguments. You can instead send an object that implements
   the NiftyJmeDisplayer protocol to provide the arguments."
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
   (nifty (asset-manager displayer)
          (input-manager displayer)
          (audio-renderer displayer)
          (view-port displayer)))
  ([asset-manager input-manager audio-renderer view-port]
   (let [display (nifty-jme-display asset-manager
                                    input-manager
                                    audio-renderer
                                    view-port)]
     (.addProcessor view-port display)
     (.getNifty display))))
      

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
  {:absolute        ElementBuilder$ChildLayoutType/Absolute
   :absolute-inside ElementBuilder$ChildLayoutType/AbsoluteInside
   :center          ElementBuilder$ChildLayoutType/Center
   :horizontal      ElementBuilder$ChildLayoutType/Horizontal
   :overlay         ElementBuilder$ChildLayoutType/Overlay
   :vertical        ElementBuilder$ChildLayoutType/Vertical})

(def align-keyword->align
  {:center ElementBuilder$Align/Center
   :left   ElementBuilder$Align/Left
   :right  ElementBuilder$Align/Right})

(def valign-keyword->valign
  {:bottom ElementBuilder$VAlign/Bottom
   :center ElementBuilder$VAlign/Center
   :top    ElementBuilder$VAlign/Top})

(def keyword->def-align-setter-form
  (partial keyword->defsetter-form
           (partial list `align-keyword->align)))

(def keyword->def-child-layout-setter-form
  (partial keyword->defsetter-form
           (partial list `child-layout-keyword->child-layout)))

(def keyword->def-valign-setter-form
  (partial keyword->defsetter-form
           (partial list `valign-keyword->valign)))

(def-directive-map element-builder-directives-map
  (let [setter-map (:setter default-directives-map)]
    (assoc default-directives-map
           :align-setter 
              (assoc setter-map
                     :form keyword->def-align-setter-form)
           :child-layout-setter
              (assoc setter-map
                     :form keyword->def-child-layout-setter-form)
           :valign-setter
              (assoc setter-map
                     :form keyword->def-valign-setter-form)
           :predicate-setter
              (assoc setter-map
                     :name predicate-keyword->setter-symbol)))
    default-director)

(defn nifty-method-invoker
  "Returns a Nifty Method Invoker that calls function f on
   invocation."
  [f]
  (proxy [NiftyMethodInvoker] [nil]
    (invoke [_] (do (f) true))
    (performInvoke [_] (f))))

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

(def-element-builder label
  {:id "label-generated LabelBuilder"
   :text "Label"}
  (LabelBuilder. id)
  {:id no-op})

(def-element-builder checkbox
  {:id "checkbox-generated CheckboxBuilder"}
  (CheckboxBuilder. id)
  {:id no-op
   :checked? (fn [^CheckboxBuilder checkbox-build
                  checked?]
               (.checked checkbox-build checked?))})

(def-element-builder scroll-panel
  {:id "scroll-panel-generated ScrollPanelBuilder"}
  (ScrollPanelBuilder. id)
  {:id no-op})
   
(def-element-builder text-field
  {:id "text-field-generated TextFieldBuilder"}
  (TextFieldBuilder. id)
  {:id no-op
   :max-length (fn [^TextFieldBuilder text-field-build
                    max-length]
                 (.maxLength text-field-build max-length))
   :password-char (fn [^TextFieldBuilder text-field-build
                       password-char]
                    (.passwordChar text-field-build password-char))})

(def-opts-constructor effect
  {:effect-name "fade"}
  (EffectBuilder. effect-name)
  {:alternate-disable (fn [^EffectBuilder effect-build
                           alternate-disable]
                        (.alternateDisable effect-build
                                           alternate-disable))
   :alternate-enable (fn [^EffectBuilder effect-build
                          alternate-enable]
                       (.alternateEnable effect-build
                                         alternate-enable))
   :effect-name no-op
   :effect-parameters (fn [^EffectBuilder effect-build
                           parameter-map]
                        (doseq [[key val] parameter-map]
                          (.effectParameter key val)))
   :inherit? (fn [^EffectBuilder effect-build
                  inherit?]
               (.inherit effect-build inherit?))
   :length (fn [^EffectBuilder effect-build
                milliseconds]
             (.length effect-build milliseconds))
   :never-stop-rendering? (fn [^EffectBuilder effect-build
                               never-stop-rendering?]
                            (.neverStopRendering effect-build
                                                 never-stop-rendering?))
   :one-shot? (fn [^EffectBuilder effect-build
                   one-shot?]
                (.oneShot effect-build one-shot?))
   :overlay? (fn [^EffectBuilder effect-build
                  overlay?]
               (.overlay effect-build overlay?))
   :post? (fn [^EffectBuilder effect-build
               post?]
            (.post effect-build post?))
   :start-delay (fn [^EffectBuilder effect-build
                     start-delay]
                  (.startDelay effect-build start-delay))
   :time-type (fn [^EffectBuilder effect-build
                   time-type]
                (.timeType effect-build time-type))})
