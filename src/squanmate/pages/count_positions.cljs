(ns squanmate.pages.count-positions
  (:require [squanmate.ui.count-position-finder :as count-positions-finder]
            [squanmate.ui.common :as common]
            [squanmate.pages.page-content :as page-content]))

(defonce state (count-positions-finder/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:h2 "Instructions:"]
     [:p
      "This page will show sliceable count positions for the layer you choose."]

     [:p "Slice positions come in two groups (only one in some cases, but most
     have two). Neither group can be said to 'have parity' or hold some other
     meaning on its own. Instead the thing that is interesting about these
     groups is that any one of a group's positions will give the " [:b "same
     parity count"] "."]
     [:p "That means you can start counting at any position in the same group
     and get the same result."]
     [common/help-block
      [common/glyphicon {:glyph :info-sign}]
      " The groups will switch colors when you rotate. When rotating, try to
      follow the shape instead of the group colors."]

     [count-positions-finder/count-position-finder state]]]])

(defmethod page-content/page :count-positions []
  [content])
