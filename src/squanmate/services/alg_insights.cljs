(ns squanmate.services.alg-insights
  "Functions for analyzing an algorithm and finding out the points of it where
  interesting things happen. These things can be e.g. highlighted to the user."
  (:require [squanmate.shapes :as shapes]))

(defn- in-cubeshape?
  ([[names step-result]]
   (= ["square" "square"] names)))

(defn- add-layer-names [step-result]
  (let [names (shapes/puzzle-layer-shape-names (:puzzle step-result))]
    [names step-result]))

;; markers for cubeshape statuses
(defrecord InCubeshape [steps])
(defrecord OutOfCubeshape [steps])

(defn- wrap-in-cubeshape-marker [group]
  (let [step-count (count group)]
    (if (in-cubeshape? (first group))
      (->InCubeshape step-count)
      (->OutOfCubeshape step-count))))

(defn entered-and-left-cubeshape
  "When you have an algorithm, it can be partially done in cubeshape (square
  square) and partially shape shifted.

  For example,
  - the alg 1/-1 returns all steps in cubeshape
  - see the tests for more good examples!

  `alg-steps` should be a sequence of execution/ step results. They must not be
  wrapped in Either values, so you must make sure they are all successful and
  extract the value before calling this function."
  [alg-steps]
  (->> alg-steps
       (map add-layer-names)
       (partition-by in-cubeshape?)
       (map wrap-in-cubeshape-marker)))
