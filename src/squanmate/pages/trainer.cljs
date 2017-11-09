(ns squanmate.pages.trainer
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.pages.page-content :as page-content]))

(defonce page-state (shape-scrambler/new-state))

(defn content []
  [:div
   [shape-scrambler/scramble-component page-state]])

(defmethod page-content/page :trainer []
  [:div
   [content]])
