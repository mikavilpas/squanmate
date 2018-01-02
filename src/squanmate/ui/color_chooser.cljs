(ns squanmate.ui.color-chooser
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as puzzle]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.services.color-settings :as color-settings]
            [squanmate.services.color-chooser :as color-chooser]))

(defn default-color-chooser-state []
  (reagent/atom newmonochrome/default-settings))

(defn- draw-mode [setting-key label state-atom]
  [common/checkbox {:checked (get-in @state-atom [:draw-mode setting-key])
                    :on-change #(swap! state-atom update-in [:draw-mode setting-key] not)}
   label])

(defn- swap-top-and-bottom-button [state-atom]
  [common/button {:on-click #(swap! state-atom update :color-settings color-chooser/do-swap-top-and-bottom)}
   "Swap top and bottom colors"])

(defn- use-back-as-front-button [state-atom]
  [common/button {:on-click #(swap! state-atom update :color-settings color-chooser/turn-y2)}
   "Use back face as front face (rotate Y2)"])

(defn- option-controls [state-atom]
  [common/form
   [common/form-group
    [common/control-label "Color options"]
    [draw-mode :monochrome? "Only gray" state-atom]

    [swap-top-and-bottom-button state-atom]
    [use-back-as-front-button state-atom]]])

(defn- puzzle-preview [state-atom]
  [newmonochrome/monochrome-puzzle puzzle/square-square @state-atom])

(defn color-chooser [state-atom]
  [:div.row
   [:div.col-xs-6
    [option-controls state-atom]]
   [:div.col-xs-6
    [puzzle-preview state-atom]]])
