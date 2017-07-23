(ns squanmate.pages.shape-visualizer
  (:require [squanmate.ui.common :as common]
            [squanmate.ui.alg-visualizer :as v]
            [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defonce alg-visualizer-state (v/default-alg-visualizer-state))

(defn- set-demo []
  ;; if you need to change something with this, it's really easy to generate a
  ;; demo state with
  ;; http://localhost:3449/cards.html#!/squanmate.ui.alg_visualizer_test/alg-visualizer
  (reset! alg-visualizer-state {:puzzle (p/->Puzzle shapes/scallop shapes/kite)
                                :initial-rotation "6"
                                :algorithm "/5,4/0,-3/"
                                :puzzle-chooser-layer-names {:bottom "kite"
                                                             :top "scallop"}}))

(defn content
  ([]
   [content alg-visualizer-state])

  ([state]
   [:div.container-fluid

    [:div.row "This page allows visualizing the different steps an algorithm has. It
  can be a useful aid when memorizing - sometimes memorizing shape transitions
  is easier than memorizing movements or numbers."]

    [:div.row
     [common/accordion
      [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :info-sign}] " Help"])
                     :bs-style "info"
                     :event-key 1}
       [:ul
        [:li "Select starting layers"]
        [:li "Rotate the layers to your starting rotation with "
         [:strong "Initial rotation"] " (won't count as a drawn step)"]
        [:li "Enter your " [:strong "algorithm"]]
        [:li "Click this to see a " [common/button {:on-click set-demo
                                                    :bs-size "xsmall"
                                                    :bs-style "primary"} "demo"]]]]]]

    [:div.row
     [:div.col-xs-6
      [v/alg-visualizer state]]]]))

(defn content-from-args [{:keys [top-shape-name
                                 bottom-shape-name
                                 initial-rotation
                                 algorithm]}]
  (let [local-state (v/default-alg-visualizer-state)]
    (swap! local-state
           merge
           {:puzzle-chooser-layer-names {:top top-shape-name
                                         :bottom bottom-shape-name}
            :algorithm algorithm
            :initial-rotation initial-rotation})
    (fn render [& _]
      [content local-state])))
