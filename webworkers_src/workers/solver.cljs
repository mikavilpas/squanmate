(ns workers.solver
  (:require [cljs-workers.worker :as worker]))

(defn- solve [{:keys [puzzle-string] :as arguments}]
  (js/importScripts "../../jaap-square1-solver/solver.js")
  (js/console.log "loaded Square-1 solver by Jaap")

  (let [solver (new js/Square1Solver)
        solution-alg-str (.solve solver puzzle-string)]
    {:result solution-alg-str}))

;; Setup the worker path
(worker/register :mirror solve)
(worker/bootstrap)
