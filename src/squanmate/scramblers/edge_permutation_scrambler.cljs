(ns squanmate.scramblers.edge-permutation-scrambler
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

(defn- puzzle-preview [state]
  (when-let [puzzle (:puzzle @state)]
    (let [draw-settings (assoc (:draw-settings @state)
                               :size 180)]
      [newmonochrome/monochrome-puzzle puzzle draw-settings])))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn- new-scramble-button [state]
  [:div
   [common/button {:on-click identity} "New scramble"]])

(defn new-default-state []
  (reagent/atom {}))

(defn scramble-component [state]
  [:div
   [:div.center
    [new-scramble-button state]
    [puzzle-preview state]]
   [:div.center.top17
    [scramble-preview (:scramble-algorithm @state)]]])
