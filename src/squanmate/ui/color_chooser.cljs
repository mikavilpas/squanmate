(ns squanmate.ui.color-chooser
  (:require [reagent.core :as reagent]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.puzzle :as puzzle]))

(defn default-color-chooser-state []
  (reagent/atom {:swap-top-and-bottom false
                 :use-back-as-front true}))

(defn- checkbox-for [setting-key label state]
  [common/checkbox {:checked (get @state setting-key)
                    :on-change #(swap! state update setting-key not)}
   label])

(defn- option-controls [state]
  [common/form
   [common/form-group
    [common/control-label "Color options"]
    [checkbox-for :swap-top-and-bottom "Swap top and bottom colors" state]
    [checkbox-for :use-back-as-front "Use back face as front face (rotate Y2)" state]]])

(defn- puzzle-preview [state]
  [newmonochrome/monochrome-puzzle puzzle/square-square
   {:monochrome? false}])

(defn color-chooser [state]
  [:div.row
   [:div.col-xs-6
    [option-controls state]]
   [:div.col-xs-6
    [puzzle-preview state]]])
