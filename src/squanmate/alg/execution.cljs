(ns squanmate.alg.execution
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.types :as types]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]))

;; possible states of the puzzle when executing an algorithm step by step
(defrecord StartingStepResult [puzzle])
(defrecord RotationStepResult [puzzle previously-applied-step])
(defrecord SliceStepResult [puzzle previously-applied-step])

;; all possible actions that can be done to advance the algorithm
(defprotocol AlgorithmStep
  (execute [this puzzle]))

(extend-type types/Rotations
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-top (rotation/rotate-layer (:top-layer puzzle)
                                            (:top-amount this))
             new-bottom (rotation/rotate-layer (:bottom-layer puzzle)
                                               (:bottom-amount this))]
            (let [new-puzzle (-> puzzle
                                 (assoc-in [:top-layer] new-top)
                                 (assoc-in [:bottom-layer] new-bottom))]
              (m/return (RotationStepResult. new-puzzle this))))))

(extend-type types/Slice
  AlgorithmStep
  (execute [this puzzle]
    (m/mlet [new-puzzle (slicing/slice puzzle)]
            (m/return (SliceStepResult. new-puzzle this)))))

(defn- execute-alg [starting-puzzle algorithm-steps]
  (reductions (fn [previous-result step]
                (m/mlet [current previous-result]
                        (execute step (:puzzle current))))
              (either/right (StartingStepResult. starting-puzzle))
              algorithm-steps))

(defn transformations
  "Takes a starting-puzzle and an algorithm as a string. Performs each step of
  the algorithm, and returns a list of steps that demonstrate how the algorithm
  was executed, step by step.

  Returns a vector of Eithers. Lefts are printable errors, rights are e.g.
  StartingStepResult etc. "
  [starting-puzzle algorithm-string]
  (let [steps-either (parser/parse algorithm-string)]
    (either/branch steps-either
                   (fn [error]
                     (vector (either/left error)))
                   (fn [algorithm-steps]
                     (execute-alg starting-puzzle algorithm-steps)))))

(defn transformation-result [starting-puzzle algorithm-string]
  (let [step-eithers (transformations starting-puzzle algorithm-string)]
    (last step-eithers)))

(defn- reverse-steps [alg-steps]
  (reverse (for [step alg-steps]
             (if (= types/Slice (type step))
               step
               (-> step
                   (update :top-amount -)
                   (update :bottom-amount -))))))

(defn transformations-reverse [starting-puzzle algorithm-string]
  (either/branch (parser/parse algorithm-string)
                 (fn [error]
                   (vector (either/left error)))
                 (fn [steps]
                   (execute-alg starting-puzzle (reverse-steps steps)))))

(defn transformation-result-reverse [starting-puzzle algorithm-string]
  (first (transformations-reverse starting-puzzle algorithm-string)))
