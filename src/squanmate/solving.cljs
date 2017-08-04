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
    (print-result (main/do-with-worker! worker {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10}}))
    ;; Copy the simple values and transfer the ArrayBuffer
    (print-result (main/do-with-worker! worker {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:d]} :transfer [:d]}))
    ;; Copy the simple values and transfer the ArrayBuffer, but transfer (browser thread) will fail cause the wrong value and the wrong type is marked to do so
    ;; (print-result (main/do-with-pool! worker {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:d]} :transfer [:c]}))
    ;; Copy the simple values and transfer the ArrayBuffer, but transfer mirroring (worker thread) will fail cause the wrong value and the wrong type is marked to do so
    ;; (print-result (main/do-with-pool! worker {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:c]} :transfer [:d]}))
    ))
