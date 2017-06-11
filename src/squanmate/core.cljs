(ns squanmate.core
  (:require [reagent.core :as reagent]
            [squanmate.pages.shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(enable-console-print!)

(def poc-url "http://localhost:9292/cubeshape/cececece?colors=yrb_yb_ybo_yo_yog_yr_ygr_yg")

(defcard-rg render-layer-proof-of-concept
  [:div [:div
         "It should be possible to display a layer of the puzzle."]
   [:div [:img {:src poc-url}]]])

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (reagent/render [:div "This is working"] node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

