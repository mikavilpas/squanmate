(ns squanmate.services.alg-insights.cubeshape
  (:require [squanmate.services.shapes :as shapes]
            [squanmate.services.alg-insights.types :as t]))

;; markers for cubeshape statuses
(defrecord InCubeshape []
  t/InsightMarker
  (id [_] :in-cubeshape)
  (description [_] "At cubeshape")
  (class-names [this]
    [(name (t/id this))]))

(defrecord ShapeShifted []
  t/InsightMarker
  (id [_] :shape-shifted)
  (description [_] "Shape shifted")
  (class-names [this]
    [(name (t/id this))]))

(defn- in-cubeshape? [puzzle]
  (= ["square" "square"] (shapes/puzzle-layer-shape-names puzzle)))

(defn- convert-to-cubeshape-marker [step-result]
  (if (in-cubeshape? (:puzzle step-result))
    (->InCubeshape)
    (->ShapeShifted)))

(defn- to-indexed-map [groups]
  (zipmap (range) groups))

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
       (map convert-to-cubeshape-marker)
       (to-indexed-map)))
