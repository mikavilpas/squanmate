(ns squanmate.ui.color-chooser
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as puzzle]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.services.color-settings :as color-settings]
            [squanmate.services.color-chooser :as color-chooser]
            [cljsjs.react-color]
            [squanmate.services.color-converter :as color-converter]))

(defn default-color-chooser-state []
  (reagent/atom newmonochrome/default-settings))

(defn- draw-mode [setting-key label state-atom]
  [common/checkbox {:checked (get-in @state-atom [:draw-mode setting-key])
                    :on-change #(swap! state-atom update-in [:draw-mode setting-key] not)}
   label])

(def ^:private color-picker (reagent/adapt-react-class
                             js/ReactColor.SketchPicker))

(defn- picker-color->rgb [picker-color]
  ;; These fields don't have the required externs so they will be munged by
  ;; advanced compilation. Using aget will prevent the optimizer from seeing
  ;; these field names, so they can be accessed.
  (let [c (aget picker-color "rgb")]
    [(aget c "r")
     (aget c "g")
     (aget c "b")]))

(defn custom-color-chooser
  "The given cursor or atom must have a squanmate color name or color, or nil."
  [cursor]
  [color-picker {:disable-alpha true
                 :color (color-converter/color->hex @cursor)
                 :on-change-complete #(reset! cursor (picker-color->rgb %))}])

(defn- color-preview [color-value]
  [:span.color.color-button
   {:style
    {:background-color (color-converter/color->hex color-value)}}])

(defn- color [label cursor]
  [common/overlay-trigger
   {:overlay (reagent/as-element [common/popover {:id "pick-color"
                                                  :title "Pick a color"}
                                  [custom-color-chooser cursor]])
    :trigger "click"
    :placement "right"}
   [common/button
    [:div.center.vertical [color-preview @cursor] label]]])

(defn- reset-defaults-button [state-atom]
  [common/button {:on-click #(reset! state-atom (deref (default-color-chooser-state)))}
   "Defaults"])

(defn- custom-colors-component [state-atom]
  [:div "Custom colors"
   (letfn [(color-of [side]
             (reagent/cursor state-atom [:color-settings side]))]
     [:div
      [color "Top"    (color-of :top)]
      [color "Bottom" (color-of :bottom)]
      [color "Left"   (color-of :left)]
      [color "Right"  (color-of :right)]
      [color "Front"  (color-of :front)]
      [color "Back"   (color-of :back)]])])

(defn- swap-top-and-bottom-button [state-atom]
  [common/button {:on-click #(swap! state-atom update :color-settings color-chooser/do-swap-top-and-bottom)}
   "Swap top and bottom colors"])

(defn- use-back-as-front-button [state-atom]
  [common/button {:on-click #(swap! state-atom update :color-settings color-chooser/turn-y2)}
   "Use back face as front face (rotate Y2)"])

(defn- quick-option-controls [state-atom]
  [common/form
   [common/form-group
    [draw-mode :monochrome? "Only gray" state-atom]

    [:div [swap-top-and-bottom-button state-atom]]
    [:div [use-back-as-front-button state-atom]]
    [:div [reset-defaults-button state-atom]]]])

(defn- puzzle-preview [state-atom]
  [newmonochrome/monochrome-puzzle puzzle/square-square @state-atom])

(defn color-chooser [state-atom]
  [:div.row
   [:div.col-xs-6
    [quick-option-controls state-atom]]
   [:div.col-xs-6
    [puzzle-preview state-atom]]
   [:div.col-xs-12
    [custom-colors-component state-atom]]])
