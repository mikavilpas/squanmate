(ns squanmate.services.google-analytics
  (:require cljsjs.google-analytics))

;; https://developers.google.com/analytics/devguides/collection/analyticsjs/single-page-applications

(def app-code "UA-105704921-1")

(defonce initialization (js/window.ga "create" app-code "auto"))

(defn send-page-view [page-url]
  (js/window.ga "set" "page" page-url)
  (js/window.ga "send" "pageview" page-url))
