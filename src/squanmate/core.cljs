(ns squanmate.core
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [reagent.core :as reagent]
            [squanmate.pages.main-ui :as main-ui]))

(enable-console-print!)

(defonce app-state (reagent/atom {:page nil}))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen EventType/NAVIGATE
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :empty))

  (defroute "/shapes" []
    (swap! app-state assoc :page :shapes))

  (hook-browser-navigation!))

(defn- try-remove-node [id]
  (when-let [element (js/document.getElementById id)]
    (-> element .-parentElement (.removeChild element))))

(defn main []
  (try-remove-node "loading-area")
  (app-routes)

  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (reagent/render [main-ui/main-ui app-state] node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html
