(ns workers.solver
  (:require [butler.core :as butler]))

(defn- solve [puzzle-string]
  ;; todo bundle and optimize this into the same file as the worker, if possible
  (js/importScripts "../../jaap-square1-solver/solver.js")

  (js/console.log "loaded Square-1 solver by Jaap")

  (let [solver (new js/Square1Solver)
        solution-alg-str (.solve solver puzzle-string)]
    {:result solution-alg-str}))

(defn run-foo [req]
  (let [solution (solve "A2B3C1D45E6F7G8H")]
    (butler/bring! :foo solution)))

(def handlers {:request-foo run-foo})

(butler/serve! handlers)
