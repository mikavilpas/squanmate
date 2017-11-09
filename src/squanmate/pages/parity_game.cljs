(ns squanmate.pages.parity-game
  (:require [squanmate.ui.parity-sequences :as ps]
            [reagent.core :as reagent]
            [squanmate.pages.page-content :as page-content]))

(defonce state (ps/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:h2 "Instructions:"]
     [:p "This is a trainer to help you recognize the parity of color sequences.
     This is a very useful skill in the Cale Schoon parity method, one that you
     will have to master if you intend to use it."]
     [ps/parity-game state]]]])

(defmethod page-content/page :parity-game []
  [content])
