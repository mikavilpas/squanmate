(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.pages.page-content :as page-content]
            [squanmate.services.global-colors-store :as global-colors-store]))

(defn- new-state []
  (scramble/default-state))

(defonce state (new-state))

(defn content
  ([]
   [content state])
  ([state-arg]
   [scramble/component state-arg @global-colors-store/settings-atom]))

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (new-state)
        scramble-algorithm (manipulation/format-alg scramble-algorithm)]
    (swap! local-state assoc-in [:scramble :scramble-algorithm] scramble-algorithm)
    (scramble/mark-alg-imported local-state)
    [content local-state]))

(defmethod page-content/page :scramble-inspector []
  [content])

(defmethod page-content/page :scramble-inspector-from-args [app-state-atom]
  [content-from-args (-> @app-state-atom
                         :page
                         :route-args)])
