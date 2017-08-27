(ns squanmate.pages.main-ui
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes :as all-possible-shapes]
            [squanmate.pages.shape-visualizer :as shape-visualizer]
            [squanmate.ui.common :as common]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.pages.trainer :as trainer]
            [squanmate.pages.importer :as importer]))

(defmulti page-content (fn [app-state]
                         (-> @app-state
                             :page
                             :name)))

;; todo move this to shapes page
(defmethod page-content :shapes []
  [all-possible-shapes/content])

(defmethod page-content :main []
  [:div
   [trainer/content]])

(defmethod page-content :shape-visualizer []
  [shape-visualizer/content])

(defmethod page-content :shape-visualizer-from-args [app-state-atom]
  [shape-visualizer/content-from-args (-> @app-state-atom
                                          :page
                                          :route-args)])

(defmethod page-content :importer []
  [importer/content])

(defmethod page-content :default [app-state]
  [:div "warning: page content not found"])

(defn navigation []
  [common/navbar
   [common/navbar-header
    [common/navbar-brand [:a {:href "#/"}
                          [:img {:src "readme/logo.png"
                                 :style {:margin-top "-5px" :width "160px"}}]]]]

   [common/nav
    [common/nav-item {:event-key 1 :href "#/shapes"} "All shapes"]
    [common/nav-item {:event-key 2 :href "#/shape-visualizer"} "Algorithm shape visualizer"]
    [common/nav-item {:event-key 3 :href "#/importer"} "Cubeshape algorithm importer"]]])

(defn- footer []
  ;; just some vertical space to make the page feel better
  [:div.bottom30])

(defn main-ui [app-state]
  [:div
   [:div.content
    [navigation]
    [:div.container
     [page-content app-state]]]

   [footer]])
