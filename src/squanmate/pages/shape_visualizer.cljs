(ns squanmate.pages.shape-visualizer
  (:require [squanmate.ui.common :as common]
            [squanmate.ui.alg-visualizer :as v]
            [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.pages.page-content :as page-content])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defonce alg-visualizer-state (v/default-alg-visualizer-state))

(defn- set-demo []
  ;; if you need to change something with this, it's really easy to generate a
  ;; demo state with
  ;; http://localhost:3449/cards.html#!/squanmate.ui.alg_visualizer_test/alg-visualizer
  (reset! alg-visualizer-state {:puzzle (shapes/puzzle-with-layers "scallop" "kite")
                                :initial-rotation "(6,6)"
                                :algorithm "/ (-1,-2)/ (-3,0)/"
                                :puzzle-chooser-layer-names {:bottom "kite"
                                                             :top "scallop"}}))

(defn content
  ([]
   [content alg-visualizer-state])

  ([state]
   [:div.container-fluid

    [:div.row "This page allows visualizing the different steps an algorithm has. It
  can be a useful aid when memorizing - sometimes memorizing shape transitions
  is easier than memorizing movements or numbers."]

    [:div.row
     [common/accordion
      [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :info-sign}] " Help"])
                     :bs-style "info"
                     :event-key 1}
       [:ul
        [:li "Select starting layers"]
        [:li "Rotate the layers to your starting rotation with "
         [:strong "Initial rotation"] " (won't count as a drawn step)"]
        [:li "Enter your " [:strong "algorithm"]]
        [:li "Click this to see a " [common/button {:on-click set-demo
                                                    :bs-size "xsmall"
                                                    :bs-style "primary"} "demo"]]]]]]

    [:div.row
     [v/alg-visualizer state]]]))

(defn content-from-args [{:keys [top-shape-name
                                 bottom-shape-name
                                 initial-rotation
                                 algorithm]}]
  (let [local-state (v/default-alg-visualizer-state)]
    (swap! local-state
           merge
           {:puzzle-chooser-layer-names {:top top-shape-name
                                         :bottom bottom-shape-name}
            :puzzle (shapes/puzzle-with-layers top-shape-name
                                               bottom-shape-name)
            :algorithm algorithm
            :initial-rotation initial-rotation})
    (fn render [& _]
      ;; It's subtle, but we're ignoring changed args here in the render
      ;; function. this makes it so that the state can only change when the page
      ;; is opened or reloaded with these args in the url.
      ;; Will have to experiment if this is desirable.
      [content local-state])))

(defmethod page-content/page :shape-visualizer []
  [content])

(defmethod page-content/page :shape-visualizer-from-args [app-state-atom]
  [content-from-args (-> @app-state-atom
                         :page
                         :route-args)])
