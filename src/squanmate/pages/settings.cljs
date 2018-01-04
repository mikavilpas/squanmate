(ns squanmate.pages.settings
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.ui.color-chooser :as color-chooser]
            [reagent.core :as reagent]))

(defonce page-state
  ;; Use a separate atom from global-colors-store/settings-atom in order to
  ;; avoid an endless loop
  (let [a (reagent/atom (global-colors-store/get-or-default!))]
    (add-watch a nil
               (fn [_ _ _ new-state]
                 (global-colors-store/save! new-state)))
    a))

(defn content []
  [:div.container
   [:div.center
    [:div.col-md-8.col-lg-8
     [:h2 "Settings"]
     [:p "Define the colors of your Square-1 here. They will saved and
     immediately taken into use anywhere Squanmate draws a puzzle."]
     [color-chooser/color-chooser page-state]]]])

(defmethod page-content/page :settings []
  [content])
