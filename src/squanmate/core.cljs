(ns squanmate.core
  (:require [reagent.core :as reagent]
            [squanmate.pages.all-possible-shapes]
            [squanmate.pages.shape-visualizer])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(enable-console-print!)

;; for now just use the devcard ui, it's good enough
(devcards.core/start-devcard-ui!)

(defn- try-remove-node [id]
  (let [element (js/document.getElementById id)]
    (-> element .-parentElement (.removeChild element))))

(defn main []
  (try-remove-node "loading-area"))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

