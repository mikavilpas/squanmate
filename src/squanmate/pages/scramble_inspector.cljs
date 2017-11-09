(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.pages.page-content :as page-content]))

(defonce state (scramble/default-state))

(defn content []
  [scramble/component state])

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (scramble/default-state)
        scramble-algorithm (manipulation/format-alg scramble-algorithm)]
    (swap! local-state assoc-in [:scramble :scramble-algorithm] scramble-algorithm)
    (scramble/mark-alg-imported local-state)
    [scramble/component local-state]))

(defmethod page-content/page :scramble-inspector []
  [content])

(defmethod page-content/page :scramble-inspector-from-args [app-state-atom]
  [content-from-args (-> @app-state-atom
                         :page
                         :route-args)])
