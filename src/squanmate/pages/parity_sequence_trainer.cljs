(ns squanmate.pages.parity-sequence-trainer
  (:require [squanmate.ui.parity-sequences :as parity-sequences]))

(defonce state (parity-sequences/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:h2 "Instructions:"]
     [:p "This is a trainer to help you recognize the parity of color sequences.
     This is a very useful skill in the Cale Schoon parity method, one that you
     will have to master if you intend to use it."]
     [parity-sequences/parity-game state]]]])
