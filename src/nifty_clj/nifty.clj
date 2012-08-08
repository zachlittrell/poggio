(ns nifty-clj.nifty
  (:use [functions.utilities :only [no-op]]
        [control.defhelpers :only [def-opts-constructor]])
  (:import [de.lessvoid.nifty Nifty]
           [de.lessvoid.nifty.builder ElementBuilder 
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

(defn set-child-layout! [^ElementBuilder element-build layout-keyword]
  (.childLayout element-build 
                (child-layout-keyword->child-layout layout-keyword)))


(def align-keyword->align
  {:center ElementBuilder$Align/Center
   :left   ElementBuilder$Align/Left
   :right  ElementBuilder$Align/Right})

(defn set-align! [^ElementBuilder element-build align-keyword]
  (.align element-build
          (align-keyword->align align-keyword)))

(def valign-keyword->valign
  {:bottom ElementBuilder$VAlign/Bottom
   :center ElementBuilder$VAlign/Center
   :top    ElementBuilder$VAlign/Top})

(defn set-valign! [^ElementBuilder element-build valign-keyword]
  (.valign element-build
           (valign-keyword->valign valign-keyword)))

(defn set-width! [^ElementBuilder element-build ^String width]
  (.width element-build width))

(defn set-height! [^ElementBuilder element-build ^String height]
  (.height element-build height))

(defn set-background-color! [^ElementBuilder element-build
                            color]
  (.backgroundColor element-build color))

(defn set-background-image! [^ElementBuilder element-build
                             backgroundImage]
  (.backgroundImage element-build backgroundImage))

(defn set-child-clip! [^ElementBuilder element-build
                       child-clip?]
  (.childClip element-build child-clip?))

(defn set-color! [^ElementBuilder element-build
                  color]
  (.color element-build color))

(defn set-controller! [^ElementBuilder element-build
                       controller]
  (.controller element-build controller))

(defn set-filename! [^ElementBuilder element-build
                     filename]
  (.filename element-build filename))


(defn set-focusable! [^ElementBuilder element-build
                      focusable?]
  (.focusable element-build focusable?))

(defn set-font! [^ElementBuilder element-build
                 font]
  (.font element-build font))

(defn set-input-mapping! [^ElementBuilder element-build
                          mapping]
  (.inputMapping element-build mapping))

(defn set-image-mode! [^ElementBuilder element-build
                       image-mode]
  (.imageMode element-build image-mode))

(defn set-inset! [^ElementBuilder element-build
                  inset]
  (.inset element-build inset))

(defn set-name! [^ElementBuilder element-build
                 name]
  (.name element-build name))

(defn set-padding! [^ElementBuilder element-build
                    padding]
  (.padding element-build padding))

(defn set-padding-bottom! [^ElementBuilder element-build
                           padding]
  (.paddingBottom element-build padding))

(defn set-padding-left! [^ElementBuilder element-build
                          padding]
  (.paddingLeft element-build padding))

(defn set-padding-right! [^ElementBuilder element-build
                          padding]
  (.paddingRight element-build padding))

(defn set-padding-top! [^ElementBuilder element-build
                          padding]
  (.paddingTop element-build padding))

(defn set-selection-color! [^ElementBuilder element-build
                            color]
  (.selectionColor element-build color))

(defn set-style! [^ElementBuilder element-build
                  style]
  (.style element-build style))

(defn set-text! [^ElementBuilder element-build
                 text]
  (.text element-build text))

(defn set-text-halign! [^ElementBuilder element-build
                       align-keyword]
  (.textHAlign element-build (align-keyword->align align-keyword)))

(defn set-text-valign! [^ElementBuilder element-build
                        valign-keyword]
  (.textVAlign element-build (valign-keyword->valign valign-keyword)))

(defn set-visible! [^ElementBuilder element-build
                    visible?]
  (.visible element-build visible?))

(defn set-visible-to-mouse! [^ElementBuilder element-build
                             visible?]
  (.visibleToMouse element-build visible?))

(defn set-x! [^ElementBuilder element-build
              x]
  (.x element-build x))

(defn set-y! [^ElementBuilder element-build
              y]
  (.y element-build y))

(defn add-images![^ElementBuilder element-build images]
  (doseq [image images] (.image element-build image)))

(defn add-panels! [^ElementBuilder element-build panels]
  (doseq [panel panels] (.panel element-build panel)))

(defn add-controls! [^ElementBuilder element-build controls]
  (doseq [control controls] (.control element-build control)))


(def element-builder-handlers
  {:align             set-align!
   :background-color  set-background-color!
   :background-image  set-background-image!
   :child-layout      set-child-layout!
   :child-clip?       set-child-clip!
   :color             set-color!
   :controls          add-controls!
   :controller        set-controller!
   :filename          set-filename!
   :focusable?        set-focusable! 
   :font              set-font!
   :height            set-height!
   :images            add-images!
   :image-mode        set-image-mode!
   :input-mapping     set-input-mapping!
   :inset             set-inset!
   :name              set-name!
   :padding           set-padding!
   :padding-bottom    set-padding-bottom!
   :padding-left      set-padding-left!
   :padding-right     set-padding-right!
   :padding-top       set-padding-top!
   :panels            add-panels!
   :selection-color   set-selection-color!
   :style             set-style!
   :text              set-text!
   :text-halign       set-text-halign!
   :text-valign       set-text-valign!
   :valign            set-valign!
   :visible?          set-visible!
   :visible-to-mouse? set-visible-to-mouse!
   :width             set-width!
   :x                 set-x!
   :y                 set-y!})

(defmacro def-element-builder [name defaults constructor handlers]
  `(def-opts-constructor ~name ~defaults ~constructor
                         (merge element-builder-handlers ~handlers)))

(def-element-builder layer
  {:id "layer generated LayerBuilder"}
  (LayerBuilder. id)
  {})

(def-element-builder panel
  {:id "panel generated PanelBuilder"}
  (PanelBuilder. id)
  {})

(def-element-builder image
  {:id "image generated ImageBuilder"}
  (ImageBuilder. id)
  {})

(def-element-builder text
  {:id "text-generated TextBuilder"}
  (TextBuilder. id)
  {:wrap? (fn [^TextBuilder text-build wrap?]
            (.wrap text-build wrap?))})

(def-element-builder button
  {:id    "button-generated ButtonBuilder"
   :label "Click here"}
  (ButtonBuilder. id label)
  {:label no-op})
