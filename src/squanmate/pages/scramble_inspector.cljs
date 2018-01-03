(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.pages.page-content :as page-content]
            [squanmate.services.global-colors-store :as global-colors-store]))

(defn- new-state []
  (let [state (scramble/default-state)
        global-colors (global-colors-store/get-or-default!)]
    (swap! state merge {:draw-settings global-colors})
    state))

(defonce state (new-state))

(defn content []
  [scramble/component state])

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (new-state)
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
