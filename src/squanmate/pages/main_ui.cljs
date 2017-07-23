(ns squanmate.pages.main-ui
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes :as all-possible-shapes]
            [squanmate.pages.shape-visualizer :as shape-visualizer]
            [squanmate.ui.common :as common]))


(defmulti page-content (fn [app-state]
                         (:page @app-state)))

;; todo move this to shapes page
(defmethod page-content :shapes []
  [all-possible-shapes/content])

(defmethod page-content :main []
  [:div])

(defmethod page-content :shape-visualizer []
  [shape-visualizer/content])

(defmethod page-content :shape-visualizer-from-args [app-state-atom]
  (shape-visualizer/content-from-args (:route-args @app-state-atom)))

(defmethod page-content :default []
  [:div "warning: page content not found"])

(defn navigation []
  [common/navbar
   [common/navbar-header
    [common/navbar-brand [:a {:href "#/"}
                          [:img {:src "readme/logo.png"
                                 :style {:margin-top "-5px" :width "160px"}}]]]]

   [common/nav
    [common/nav-item {:event-key 1 :href "#/shapes"} "All shapes"]
    [common/nav-item {:event-key 2 :href "#/shape-visualizer"} "Algorithm shape visualizer"]]])

(defn- footer []
  ;; just some vertical space to make the page feel better
  [:div.bottom30])

(defn main-ui [app-state]
  [:div
   [:div.content
    [:div
     [navigation]
     [:div.container
      [page-content app-state]]]]

   [footer]])
