(ns squanmate.solving
  (:require [butler.core :as butler]))

;; there's a different worker file for development / debugging, and production
(defonce worker-path ;; (if js/goog.DEBUG
                     ;;   "js/compiled/bootstrap_worker.js"
                     ;;   "js/compiled/webworkers.js")
  "js/compiled/webworkers.js")

(enable-console-print!)

;; Create a web worker with script and handlers.
(def example-butler (butler/butler worker-path {:foo (fn [res]
                                                       (println res))}))

(defn example-call []
  (butler/work! example-butler :request-foo "foo"))
