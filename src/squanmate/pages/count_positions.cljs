(ns squanmate.pages.count-positions
  (:require [squanmate.ui.count-position-finder :as count-positions-finder]))

(defonce state (count-positions-finder/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:p
      [:h3 "Instructions:"]
      "This page will show sliceable count positions for the layer you choose."]

     [:p "Slice positions come in two groups (only one in some cases, but most
     have two). Neither group can be said to 'have parity' or hold some other
     meaning on its own. Instead the thing that is interesting about these
     groups is that any one of a group's positions will give the " [:b "same
     parity count"] "."]
     [:p "That means you can start counting at any position in the same group
     and get the same result."]

     [count-positions-finder/count-position-finder state]]]])
