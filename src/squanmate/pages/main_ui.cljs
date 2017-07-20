(ns squanmate.pages.main-ui
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes :as all-possible-shapes]
            [squanmate.ui.common :as common]))

(defmulti page-content (fn [app-state]
                         (:page @app-state)))

;; todo move this to shapes page
(defmethod page-content :shapes []
  [all-possible-shapes/all-possible-shapes])

(defmethod page-content :default []
  [:div])

(defn navigation []
  [common/navbar
   [common/navbar-header
    [common/navbar-brand [:a {:href "#/"} "Squanmate"]]]

   [common/nav
    [common/nav-item {:event-key 1 :href "#/shapes"}
     "All shapes"]]])

(defn- footer []
  ;; just some vertical space to make the page feel better
  [:div.bottom30])

(defn main-ui [app-state]
  [:div
   [:div.content
    [:div.container
     [navigation]
     [:div.container
      [page-content app-state]]]]

   [footer]])
