(ns squanmate.alg.execution
  (:require [squanmate.alg.types :as types]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]
            [cats.core :as m]
            [squanmate.alg.parser :as parser]
            [cats.monad.either :as either]))

(defrecord ExecutionState [puzzle previously-applied-step])

(defprotocol AlgorithmStep
  (execute [this puzzle]))

(extend-type types/RotateTopLayer
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-layer (rotation/rotate-layer (:top-layer puzzle)
                                              (:amount this))]
            (let [new-puzzle (assoc-in puzzle [:top-layer] new-layer)]
              (m/return (ExecutionState. new-puzzle this))))))

(extend-type types/RotateBottomLayer
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-layer (rotation/rotate-layer (:bottom-layer puzzle)
                                              (:amount this))]
            (let [new-puzzle (assoc-in puzzle [:bottom-layer] new-layer)]
              (m/return (ExecutionState. new-puzzle this))))))

(extend-type types/Slice
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-puzzle (slicing/slice puzzle)]
            (m/return (ExecutionState. new-puzzle this)))))

(defn transformations [starting-puzzle algorithm-string]
  (let [start (either/right starting-puzzle)
        puzzle-states (m/mlet [algorithm-steps (parser/parse algorithm-string)]
                              (reductions (fn [previous-result step]
                                            (m/mlet [current previous-result]
                                                    (execute step current)))
                                          start
                                          algorithm-steps))]
    (list* start puzzle-states)))
