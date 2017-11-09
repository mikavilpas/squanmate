(ns squanmate.pages.algorithm-trainer
  (:require [squanmate.scramblers.alg-trainer :as alg-trainer]
            [squanmate.pages.page-content :as page-content]))

(defonce page-state (alg-trainer/new-default-state))

(defn content []
  [:div
   [alg-trainer/trainer-component page-state]])

(defmethod page-content/page :algorithm-trainer []
  [:div
   [content]])
