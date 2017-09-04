(ns squanmate.ui.color-chooser
  (:require [reagent.core :as reagent]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.puzzle :as puzzle]))

(defn default-color-chooser-state []
  (reagent/atom {:swap-top-and-bottom false
                 :use-back-as-front true
                 :monochrome? false}))

(defn- checkbox-for [setting-key label state-atom]
  [common/checkbox {:checked (get @state-atom setting-key)
                    :on-change #(swap! state-atom update setting-key not)}
   label])

(defn- option-controls [state-atom]
  [common/form
   [common/form-group
    [common/control-label "Color options"]
    [checkbox-for :monochrome? "Only gray" state-atom]
    [checkbox-for :swap-top-and-bottom "Swap top and bottom colors" state-atom]
    [checkbox-for :use-back-as-front "Use back face as front face (rotate Y2)" state-atom]]])

(defn- puzzle-preview [state-atom]
  [newmonochrome/monochrome-puzzle puzzle/square-square @state-atom])

(defn color-chooser [state-atom]
  [:div.row
   [:div.col-xs-6
    [option-controls state-atom]]
   [:div.col-xs-6
    [puzzle-preview state-atom]]])
