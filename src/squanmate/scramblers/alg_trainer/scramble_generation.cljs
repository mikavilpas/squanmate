(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as rotation]
            [squanmate.solving :as solving]
            [squanmate.scramblers.algsets.edge-permutation :as ep]))

(defn- apply-starting-rotation [puzzle]
  (letfn [(rotate [layer]
            (let [[new-layer _amount] (rand-nth (rotation/possible-rotations
                                                 layer
                                                 [0 3 -3 6]))]
              new-layer))]
    (update puzzle
            :top-layer rotate
            :bottom-layer rotate)))

(defn- report-error-for-case [alg e]
  (js/console.log "Error setting up case for alg: " alg ". Description: " e))

(defn- set-scramble-for-start-position! [state end-step-result]
  (let [puzzle (:puzzle end-step-result)]
    (solving/solve-and-generate-scramble puzzle state)))

(defn- random-case [state]
  (let [cases (:selected-cases @state)
        case-name (rand-nth (seq cases))
        c (get ep/all-cases-unordered case-name)]
    c))

(defn new-scramble
  ([state]
   (new-scramble state (random-case state)))
  ([state alg]
   ;; a starting rotation makes it possible to get the same EP case in all
   ;; different orientations
   (let [start (apply-starting-rotation p/square-square)

         alg-starting-state (execution/transformation-result-reverse start alg)]
     (swap! state assoc :scramble-algorithm "")
     (either/branch alg-starting-state
                    #(report-error-for-case alg %)
                    #(set-scramble-for-start-position! state %)))))
