(ns squanmate.pages.trainer
  (:require [reagent.core :as reagent]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.ui.common :as common]))

(defonce page-state (shape-scrambler/new-state))

(defn new-scramble-button [state]
  [common/button {:on-click #(shape-scrambler/new-scramble! state)}
   "New scramble"])

(defn content []
  [:div
   [:div.center.bottom17
    [new-scramble-button page-state]]
   [shape-scrambler/scramble-component page-state]])
