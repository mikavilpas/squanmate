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
    (swap! app-state assoc :page :empty))

  (defroute "/shapes" []
    (swap! app-state assoc :page :shapes))

  (hook-browser-navigation!))
