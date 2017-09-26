(ns squanmate.pages.trainer
  (:require [reagent.core :as reagent]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.ui.common :as common]
            [squanmate.pages.links :as links]
            [squanmate.services.google-analytics :as ga]))

(defonce page-state (shape-scrambler/new-state))

(defn- set-new-scramble [state]
  (shape-scrambler/new-scramble! state)
  (ga/send-page-view :trainer/new-scramble))

(defn new-scramble-button [state]
  [:div.center
   [common/button {:on-click #(set-new-scramble state)
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
