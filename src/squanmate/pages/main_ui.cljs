(ns squanmate.pages.main-ui
  (:require [squanmate.pages.page-content :as page-content]
            [reagent.core :as reagent]

            ;; note: all pages need to be referenced here, so that their page
            ;; definition is loaded!

            [squanmate.ui.common :as common]
            [squanmate.pages.all-possible-shapes :as all-possible-shapes]
            [squanmate.pages.shape-visualizer :as shape-visualizer]
            [squanmate.pages.trainer :as trainer]
            [squanmate.pages.importer :as importer]
            [squanmate.pages.count-positions :as count-positions]
            [squanmate.pages.parity-game :as parity-game]
            [squanmate.pages.scramble-inspector :as scramble-inspector]
            [squanmate.pages.algorithm-trainer :as algorithm-trainer]))

(defmethod page-content/page :default [app-state]
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
     [common/nav-item {:event-key 1 :href "#/algorithm-trainer"} "Algorithm trainer"]
     [common/nav-item {:event-key 2 :href "#/shapes"} "All shapes"]
     [common/nav-item {:event-key 3 :href "#/scramble-inspector"} "Scramble inspector"]
     [common/nav-item {:event-key 4 :href "#/shape-visualizer"} "Algorithm shape visualizer"]
     [common/nav-item {:event-key 5 :href "#/importer"} "Cubeshape algorithm importer"]
     [common/nav-item {:event-key 6 :href "#/count-positions"} "Parity count positions"]
     [common/nav-item {:event-key 7 :href "#/parity-game"} "Parity game"]]]])

(defn- footer []
  ;; just some vertical space to make the page-content/page feel better
  [:div.bottom30])

(defn main-ui [app-state]
  [:div
   [:div.content
    [navigation]
    [:div.container
     [page-content/page app-state]]]

   [footer]])
