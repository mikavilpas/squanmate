(ns squanmate.alg.execution
  (:require [squanmate.alg.types :as types]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]
            [cats.core :as m]))

(defprotocol AlgorithmStep
  (execute [this puzzle]))

(extend-type types/RotateTopLayer
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-layer (rotation/rotate-layer (:top-layer puzzle)
                                              (:amount this))]
            (m/return (assoc-in puzzle [:top-layer] new-layer)))))

(extend-type types/RotateBottomLayer
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-layer (rotation/rotate-layer (:bottom-layer puzzle)
                                              (:amount this))]
            (m/return (assoc-in puzzle [:bottom-layer] new-layer)))))

(extend-type types/Slice
  AlgorithmStep
  (execute [this puzzle]
    (slicing/slice puzzle)))
