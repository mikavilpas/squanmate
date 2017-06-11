(ns squanmate.core
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(enable-console-print!)

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (reagent/render [:div "This is working"] node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

