(ns squanmate.pages.algorithm-trainer
  (:require [squanmate.scramblers.alg-trainer :as alg-trainer]))

(defonce page-state (alg-trainer/new-default-state))

(defn content []
  [:div
   [alg-trainer/trainer-component page-state]])
