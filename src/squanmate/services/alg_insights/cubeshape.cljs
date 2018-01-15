(ns squanmate.services.alg-insights.cubeshape
  (:require [squanmate.services.shapes :as shapes]
            [squanmate.services.alg-insights.types :as t]))

(defn- in-cubeshape?
  ([[names step-result]]
   (= ["square" "square"] names)))

(defn- add-layer-names [step-result]
  (let [names (shapes/puzzle-layer-shape-names (:puzzle step-result))]
    [names step-result]))

;; markers for cubeshape statuses
(defrecord InCubeshape []
  t/InsightMarker
  (id [_] :in-cubeshape)
  (description [_] "Here the puzzle is at cubeshape"))

(defrecord ShapeShifted []
  t/InsightMarker
  (id [_] :shape-shifted)
  (description [_] "Here the puzzle is shape shifted"))

(defn- convert-to-cubeshape-markers-by-index [groups]
  (loop [markers (hash-map)
         groups groups
         index 0]
    (if-let [g (first groups)]
      (let [step-count (count g)
            marker (if (in-cubeshape? (first g))
                     (->InCubeshape)
                     (->ShapeShifted))
            group-steps (zipmap (range index (+ index step-count))
                                (repeat marker))]
        (recur (into markers group-steps)
               (next groups)
               (+ index step-count)))
      markers)))

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
       convert-to-cubeshape-markers-by-index))
