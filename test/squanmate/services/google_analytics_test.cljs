(ns squanmate.services.google-analytics-test
  (:require [squanmate.services.google-analytics :as ga]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (reagent/atom {:page-url "TEST/"}))

(defcard-rg test
  [:div "Test the analytics plugin here, but don't go overboard pls. All results
  will be shown in the google analytics view as test views, but still are taken
  into account along with normal views. "
   [:p
    [:strong "Consider keeping in the TEST/ prefix to separate test views from
   real views."]]

   [common/input-box
    (reagent/cursor state [:page-url]) "Simulated page url"]

   [common/button {:on-click #(ga/send-page-view (:page-url @state))}
    "Send page view to Google Analytics"]])
