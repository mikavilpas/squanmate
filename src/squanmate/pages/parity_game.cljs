(ns squanmate.pages.parity-game
  (:require [squanmate.ui.parity-sequences :as ps]
            [reagent.core :as reagent]
            [squanmate.pages.page-content :as page-content]
            [squanmate.services.keyboard :as keyboard]
            [squanmate.ui.common :as common]))

(defonce state (ps/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:h2 "Instructions:"]
     [:p "This is a trainer to help you recognize the parity of color sequences.
     This is a very useful skill in the Cale Schoon parity method, one that you
     will have to master if you intend to use it."]
     [common/help-block [common/glyphicon {:glyph :info-sign}]
      " You can use the left and right keys to answer too"]
     [ps/parity-game state]]]])

(defmethod page-content/page :parity-game []
  (keyboard/bind! "left" #(ps/answer-parity state))
  (keyboard/bind! "right" #(ps/answer-no-parity state))
  [content])
