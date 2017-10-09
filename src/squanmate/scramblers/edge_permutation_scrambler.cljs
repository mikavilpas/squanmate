(ns squanmate.scramblers.edge-permutation-scrambler
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]))

(defn- puzzle-preview [state]
  (let [draw-settings (assoc (:draw-settings @state)
                             :size 180)
        puzzle (:puzzle @state)]
    [newmonochrome/monochrome-puzzle puzzle draw-settings]))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn scramble-component [state]
  [:div
   [:div.center
    [puzzle-preview state]]
   [:div.center
    [scramble-preview (:scramble-algorithm @state)]]])
