(ns squanmate.routing
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [reagent.core :as reagent]
            [squanmate.pages.main-ui :as main-ui]))

(defonce browser-setup? (reagent/atom false))

(defn- hook-browser-navigation! []
  (when-not @browser-setup?
    (doto (History.)
      (events/listen EventType/NAVIGATE
                     (fn [event]
                       (secretary/dispatch! (.-token event))))
      (.setEnabled true))
    (reset! browser-setup? true)))

(defn app-routes [app-state]
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :main))

  (defroute "/shapes" []
    (swap! app-state assoc :page :shapes))

  (defroute "/shape-visualizer/:top-shape-name/:bottom-shape-name/:initial-rotation/:algorithm"
    {:as route-args}
    ;; This route displays the visualization with the data provided in the URL
    ;; itself. Can be used to link visualizations to other users or for personal
    ;; reference.
    (swap! app-state assoc :route-args route-args)
    (swap! app-state assoc :page :shape-visualizer-from-args))

  (defroute "/shape-visualizer" []
    (swap! app-state assoc :page :shape-visualizer))

  (hook-browser-navigation!))
