(ns workers.solver
  (:require [cljs-workers.worker :as worker]))

;; Setup the worker path
(worker/register :mirror
                 (fn [& arguments]
                   (js/console.log "received arguments: " (str arguments))
                   (fn [& arguments2]
                     arguments)))

(js/console.log "hello from solver 234!")
(js/console.log (str @worker/handlers))

(worker/bootstrap)
