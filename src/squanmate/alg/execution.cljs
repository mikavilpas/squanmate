(ns squanmate.alg.execution
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.types :as types]
            [squanmate.alg.rotation :as rotation]
            [squanmate.slicing :as slicing]
            [squanmate.alg.manipulation :as manipulation]))

;; possible states of the puzzle when executing an algorithm step by step
(defrecord StartingStepResult [puzzle])
(defrecord RotationStepResult [puzzle previously-applied-step])
(defrecord SliceStepResult [puzzle previously-applied-step])

(defn successful-transformations? [step-eithers]
  (every? either/right? step-eithers))

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

(defn transformations-reverse [starting-puzzle algorithm-string]
  (either/branch (parser/parse algorithm-string)
                 (fn [error]
                   (vector (either/left error)))
                 (fn [steps]
                   (execute-alg starting-puzzle (manipulation/reverse-steps steps)))))

(defn transformation-result-reverse [starting-puzzle algorithm-string]
  (last (transformations-reverse starting-puzzle algorithm-string)))

(defn puzzle-of-result
  "Only use this if you know the transformation should have been successful (no
  chance of error basically). Throws a strange error if a problem occurs."
  [transformation-result-either]
  (:puzzle (m/extract transformation-result-either)))
