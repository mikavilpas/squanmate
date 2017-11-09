(ns squanmate.pages.importer
  (:require [squanmate.ui.alg-importer :as alg-importer]
            [squanmate.pages.page-content :as page-content]))

(def importer-state (alg-importer/default-alg-importer-state))

(defn content []
  [:div.container
   [alg-importer/ui importer-state]])

(defmethod page-content/page :importer []
  [content])
