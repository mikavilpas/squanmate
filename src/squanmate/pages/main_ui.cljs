(ns squanmate.pages.main-ui
  (:require [reagent.core :as reagent]
            [squanmate.ui.common :as common]))

(defmulti page-content (fn [app-state]
                         (let [page (:page @app-state)]
                           (println "currently recognized at page " page)
                           page)))

;; todo move this to shapes page
(defmethod page-content :shapes []
  [:div "todo: draw shapes page here"])

(defmethod page-content :default []
  [:div])

(defn navigation []
  [common/navbar
   [common/navbar-header
    [common/navbar-brand [:a {:href "#/"} "Squanmate"]]]

   [common/nav
    [common/nav-item {:event-key 1 :href "#/shapes"}
     "All shapes"]]])

(defn main-ui [app-state]
  [:div
   [navigation (:page @app-state)]
   [page-content app-state]])
