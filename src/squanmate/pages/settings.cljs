(ns squanmate.pages.settings
  (:require [squanmate.pages.page-content :as page-content]))

;; TODO write a color settings page

(defn content []
  [:div "todo"])

(defmethod page-content/page :settings []
  [:div
   [content]])
