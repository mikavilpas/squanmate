(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as rotation]
            [squanmate.solving :as solving]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.services.google-analytics :as ga]))

(defn- apply-starting-rotation [puzzle]
  (letfn [(rotate [layer]
            (let [[new-layer _amount] (rand-nth (rotation/possible-rotations
                                                 layer
                                                 [0 3 -3 6]))]
              new-layer))]
    (-> puzzle
        (update :top-layer rotate)
        (update :bottom-layer rotate))))

(defn- report-error-for-case [alg e]
  (js/console.log "Error setting up case for alg: " alg ". Description: " e))

(defn- set-scramble-for-start-position! [state end-step-result]
  (let [puzzle (:puzzle end-step-result)]
    (swap! state assoc :puzzle puzzle)
    (solving/solve-and-generate-scramble puzzle state)))

(defn get-case [case-name]
  (let [alg (get ep/all-cases case-name)]
    [case-name alg]))

(defn- random-case [state]
  (let [cases (:selected-cases @state)
        case-name (rand-nth (seq cases))]
    case-name))

(defn new-scramble
  ([state]
   (new-scramble state (random-case state)))
  ([state case-name]
   ;; a starting rotation makes it possible to get the same EP case in all
   ;; different orientations
   (let [[case-name alg] (get-case case-name)
         start (apply-starting-rotation p/square-square)
         alg-starting-state (execution/transformation-result-reverse start alg)]
     (swap! state assoc :scramble-algorithm ""
            :chosen-case-name case-name)
     (either/branch alg-starting-state
                    #(report-error-for-case alg %)
                    #(set-scramble-for-start-position! state %)))))

(defn set-new-scramble! [& args]
  (apply new-scramble args)
  (ga/send-page-view :algorithm-trainer/new-scramble))
