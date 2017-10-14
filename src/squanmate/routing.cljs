(ns squanmate.routing
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [squanmate.services.google-analytics :as ga]
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

(defn- set-route!
  ([app-state page]
   (set-route! app-state page []))
  ([app-state page route-args]
   (swap! app-state assoc :page {:name page
                                 :route-args route-args})
   (ga/send-page-view page)))

(defn app-routes [app-state]
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (set-route! app-state :trainer))

  (defroute "/algorithm-trainer" []
    (set-route! app-state :algorithm-trainer))

  (defroute "/shapes" []
    (set-route! app-state :shapes))

  (defroute "/shape-visualizer" []
    (set-route! app-state :shape-visualizer))

  (defroute "/shape-visualizer/:top-shape-name/:bottom-shape-name/:initial-rotation/:algorithm"
    {:as route-args}
    ;; This route displays the visualization with the data provided in the URL
    ;; itself. Can be used to link visualizations to other users or for personal
    ;; reference.
    (set-route! app-state :shape-visualizer-from-args route-args))

  (defroute "/importer" []
    (set-route! app-state :importer))

  (defroute "/count-positions" []
    (set-route! app-state :count-positions))

  (defroute "/parity-game" []
    (set-route! app-state :parity-game))

  (defroute "/scramble-inspector" []
    (set-route! app-state :scramble-inspector))

  (defroute "/scramble-inspector/:scramble-algorithm" {:as route-args}
    (set-route! app-state :scramble-inspector-from-args route-args))

  (hook-browser-navigation!))
