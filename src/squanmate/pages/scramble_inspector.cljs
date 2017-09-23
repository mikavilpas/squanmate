(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]))

(defonce state (scramble/default-state))

(defn content []
  [scramble/component state])

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (scramble/default-state)
        scramble-algorithm (manipulation/format-alg scramble-algorithm)]
    (swap! local-state #(-> %
                            (assoc :imported? true)
                            (assoc-in [:scramble :scramble-algorithm] scramble-algorithm)))
    [scramble/component local-state]))
