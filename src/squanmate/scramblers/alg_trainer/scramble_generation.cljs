(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.alg.rotation :as rotation]
            [squanmate.services.solving :as solving]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.services.google-analytics :as ga]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types]))

(defn- rotate-square-layer
  "Rotates and keeps the expected orientation of the layer so scrambles can be
  generated from it."
  [layer]
  (rand-nth (rotation/possible-rotations
             layer
             [0 3 -3 6])))

(defn- apply-starting-rotation [puzzle]
  (letfn [(rotate [layer]
            (let [[new-layer _amount] (rotate-square-layer layer)]
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

(defn- random-case [state]
  (let [cases (:selected-cases @state)
        case (rand-nth (seq cases))]
    case))

(defn- prepend-random-rotations [alg-string]
  ;; note: the given alg-string must start at square-square
  (letfn [(random-rotation-amount [layer]
            (let [[_new-layer amount] (rotate-square-layer layer)]
              amount))]
    (let [top-amount (-> p/square-square :top-layer random-rotation-amount)
          bottom-amount (-> p/square-square :bottom-layer random-rotation-amount)
          rotations (types/->Rotations top-amount bottom-amount)]
      (manipulation/try-update-alg-string alg-string
                                          (partial manipulation/prepend-initial-rotation rotations)))))

(defn- add-initial-rotation-to-case [case]
  (let [[case-name alg] case
        rotated-alg (prepend-random-rotations alg)]
    [case-name rotated-alg]))

(defn new-scramble
  ([state]
   (new-scramble state (random-case state)))
  ([state case]
   ;; a starting rotation makes it possible to get the same EP case in all
   ;; different orientations
   (let [case (add-initial-rotation-to-case case)
         [case-name alg] case
         start (apply-starting-rotation p/square-square)
         alg-starting-state (execution/transformation-result-reverse start alg)]
     (swap! state assoc :scramble-algorithm ""
            :chosen-case case)
     (either/branch alg-starting-state
                    #(report-error-for-case alg %)
                    #(set-scramble-for-start-position! state %)))))

(defn set-new-scramble! [& args]
  (apply new-scramble args)
  (ga/send-page-view :algorithm-trainer/new-scramble))
