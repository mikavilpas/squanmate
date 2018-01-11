(ns squanmate.services.alg-insights
  "Functions for analyzing an algorithm and finding out the points of it where
  interesting things happen. These things can be e.g. highlighted to the user."
  (:require [squanmate.shapes :as shapes]
            [cats.core :as m]
            [squanmate.alg.parser :as parser]
            [squanmate.utils.either-utils :as eu]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]))

(defn- in-cubeshape?
  ([[names step-result]]
   (= ["square" "square"] names)))

(defn- add-layer-names [step-result]
  (let [names (shapes/puzzle-layer-shape-names (:puzzle step-result))]
    [names step-result]))

;; markers for cubeshape statuses
(def in-cubeshape :in-cubeshape)
(def shape-shifted :shape-shifted)

(defn- convert-to-cubeshape-markers-by-index [groups]
  (loop [markers (hash-map)
         groups groups
         index 0]
    (if-let [g (first groups)]
      (let [step-count (count g)
            marker (if (in-cubeshape? (first g))
                     in-cubeshape
                     shape-shifted)]
        (recur (assoc markers index marker)
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

(defn- alg-with-markers
  "Combines the parsed parts of the scramble algorithm "
  [display-tokens markers]
  )

(defn- scramble-steps [alg-string]
  (let [alg-steps (execution/transformations p/square-square alg-string)
        steps-either (eu/list-of-eithers->either-list alg-steps)]
    steps-either))

(defn alg-with-cubeshape-status-highlighted [alg-string]
  (m/mlet [display-tokens (parser/parse alg-string)
           execution-steps (scramble-steps alg-string)]
          (m/return
           (let [markers (entered-and-left-cubeshape execution-steps)]
             (alg-with-markers display-tokens markers)))))
