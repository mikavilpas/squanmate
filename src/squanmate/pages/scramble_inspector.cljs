(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]))

(defonce state (scramble/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-xs-8
     [scramble/component state]]]])

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (scramble/default-state)
        scramble-algorithm (manipulation/format-alg scramble-algorithm)]
    (swap! local-state scramble/mark-alg-imported)
    [scramble/component local-state]))
