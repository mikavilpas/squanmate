(ns squanmate.alg.types)

(defprotocol AlgorithmStep
  (execute [this puzzle]))

(defrecord Slice []
  AlgorithmStep
  (execute [this puzzle]))

(defrecord RotateTopLayer [amount]
  AlgorithmStep
  (execute [this puzzle]))

(defrecord RotateBottomLayer [amount]
  AlgorithmStep
  (execute [this puzzle]))
