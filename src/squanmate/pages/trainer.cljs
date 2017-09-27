(ns squanmate.pages.trainer
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]))

(defonce page-state (shape-scrambler/new-state))

(defn content []
  [:div
   [shape-scrambler/scramble-component page-state]])
