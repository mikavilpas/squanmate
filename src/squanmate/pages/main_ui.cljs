(ns squanmate.pages.main-ui
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes :as all-possible-shapes]
            [squanmate.pages.shape-visualizer :as shape-visualizer]
            [squanmate.ui.common :as common]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.pages.trainer :as trainer]
            [squanmate.pages.importer :as importer]
            [squanmate.pages.count-positions :as count-positions]
            [squanmate.pages.parity-game :as parity-game]
            [squanmate.pages.scramble-inspector :as scramble-inspector]
            ))

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

(defmethod page-content :count-positions []
  [count-positions/content])

(defmethod page-content :parity-game []
  [parity-game/content])

(defmethod page-content :scramble-inspector []
  [scramble-inspector/content])

(defmethod page-content :default [app-state]
  [:div "warning: page content not found"])

(defn navigation []
  [common/navbar
   [common/navbar-header
    [common/navbar-brand [:a {:href "#/"}
                          [:img {:src "readme/logo.png"
                                 :style {:margin-top "-5px" :width "160px"}}]]]
    [common/navbar-toggle]]

   [common/navbar-collapse
    [common/nav
     [common/nav-item {:event-key 1 :href "#/shapes"} "All shapes"]
     [common/nav-item {:event-key 2 :href "#/shape-visualizer"} "Algorithm shape visualizer"]
     [common/nav-item {:event-key 3 :href "#/importer"} "Cubeshape algorithm importer"]
     [common/nav-item {:event-key 4 :href "#/count-positions"} "Parity count positions"]
     [common/nav-item {:event-key 5 :href "#/parity-game"} "Parity game"]
     [common/nav-item {:event-key 6 :href "#/scramble-inspector"} "Scramble inspector"]]]])

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
