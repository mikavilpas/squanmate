(ns squanmate.solving
  (:require [cljs.core.async :refer [<!]]
            [cljs-workers.core :as main])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]))

;; there's a different worker file for development / debugging, and production
(defonce worker-path (if js/goog.DEBUG
                       "js/bootstrap_worker.js"
                       "js/compiled/webworkers.js"))

(defn app []
  (let [;; you can create one worker or a pool (async channel of workers)
        worker
        (main/create-one worker-path)

        ;; a "do-with-pool" or "-worker" (see below) will return immediately and
        ;; give you a result channel. So to print the result you have to handle
        ;; the channel
        print-result
        (fn [result-chan]
          (go
            (let [result (<! result-chan)]
              (js/console.log (str (:state result)) (str result)))))]

    ;; Copy all simple values
    (print-result (main/do-with-worker! worker {:handler :mirror, :arguments {:puzzle-string "A2B3C1D45E6F7G8H"}}))))
