(ns squanmate.pages.shape-visualizer
  (:require [squanmate.puzzle :as puzzle]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [squanmate.shapes :as s]
            [squanmate.ui.alg-visualizer :as v]
            [reagent.core :as reagent]
            [cljsjs.react-bootstrap])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def accordion (reagent/adapt-react-class js/ReactBootstrap.Accordion))
(def glyphicon (reagent/adapt-react-class js/ReactBootstrap.Glyphicon))

(defonce alg-visualizer-state (v/default-alg-visualizer-state))

(defcard-rg introduction
  [:div.container-fluid

   [:div.row "This page allows visualizing the different steps an algorithm has. It
  can be a useful aid when memorizing - sometimes memorizing shape transitions
  is easier than memorizing movements or numbers."]

   [:div.row
    [accordion
     [v/panel {:header (reagent/as-element [:span [glyphicon {:glyph :info-sign}] " Help"])
               :bs-style "info"
               :event-key 1}
      [:ul
       [:li "Select starting layers"]
       [:li "Rotate the layers to your starting rotation with " [:strong "Initial rotation"] " (won't count as a drawn step)"]
       [:li "Enter your " [:strong "algorithm"]]]]]]

   [:div.row
    [:div.col-xs-6
     [v/alg-visualizer alg-visualizer-state]]]])
