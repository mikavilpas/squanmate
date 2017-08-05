(ns workers.solver
  (:require [butler.core :as butler]))

(js/importScripts "../../jaap-square1-solver/solver.js")

(defn solve [puzzle-string]
  ;; todo bundle and optimize this into the same file as the worker, if possible

  (js/console.log "loaded Square-1 solver by Jaap")

  (let [;; solver (new js/Square1Solver)
        ;; this survives the google closure compiler advanced optimizations,
        ;; although it looks pretty ugly
        ;; solution-alg-str (.call (aget solver "solve") solver puzzle-string)
        ]
    ;; (js/console.log "found raw solution: " solution-alg-str)
    "test"))

(defn run-foo [req]

  ;; (new js/Square1Solver)
  (butler/bring! :foo "test"))

(def handlers {:request-foo run-foo})

(butler/serve! handlers)
