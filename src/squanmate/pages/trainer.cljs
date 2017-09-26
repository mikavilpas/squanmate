(ns squanmate.pages.trainer
  (:require [reagent.core :as reagent]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.ui.common :as common]
            [squanmate.pages.links :as links]))

(defonce page-state (shape-scrambler/new-state))

(defn new-scramble-button [state]
  [:div.center
   [common/button {:on-click #(shape-scrambler/new-scramble! state)
                   :bs-style :success}
    "New scramble"]
   [common/button {:on-click #(links/set-link-to-scramble (:scramble-algorithm @state))}
    [common/glyphicon {:glyph :search}]
    " Inspect"]])

(defn content []
  [:div
   [:div.bottom17
    [new-scramble-button page-state]]
   [shape-scrambler/scramble-component page-state]])
