(ns squanmate.utils.alg-verification-utils
  "Tools for verifying the validity of algorithms."
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.alg.execution :as e]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.puzzle :as p]
            [squanmate.alg.rotation :as rotation]
            [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as pps]
            [squanmate.services.cube-aligner :as cube-aligner]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]
            [squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset :as pra]))

(defn- parse [[case-name alg]]
  [case-name (parser/parse alg)])

(defn non-parseable-cases [cases]
  (let [parse-results (map parse cases)]
    (filter (fn [[case-name result]]
              (either/left? result))
            parse-results)))

(defn- execute-reverse [[case-name alg]]
  [case-name
   (e/transformation-result-reverse p/square-square
                                    alg)])

(defn non-executable-cases [cases]
  (let [execution-results (map execute-reverse cases)]
    (->> execution-results
         (filter (fn [[case-name result]]
                   (either/left? result))))))

(defn- rotations-for-alg [alg]
  (m/mlet [end-step (e/transformation-result p/square-square
                                             alg)
           rotations (cube-aligner/rotations-to-align-cube (:puzzle end-step))]
          rotations))

(defn non-aligned-cases [cases]
  (for [[case-name alg] cases
        :let [rotations (rotations-for-alg alg)]
        :when (not (= rotations rotation/empty-rotation))]
    [case-name rotations]))

(defn alg-switches-parity-at-layer-default-positions?
  "For an algorithm that is known to start and end at square square, returns an
  Either[keyword] describing whether the algorithm has caused the puzzle to
  switch parity."
  [alg-string]
  (let [steps (e/transformations p/square-square alg-string)]
    (m/mlet [start (first steps)
             end (last steps)
             start-parity (pps/puzzle-parity-at-default-layer-positions (:puzzle start))
             end-parity (pps/puzzle-parity-at-default-layer-positions (:puzzle end))
             keeps-parity? (either/right (= start-parity end-parity))]
            (m/return
             (if keeps-parity?
               :preserves-parity
               :switches-parity)))))

(defn parity-and-non-parity-cases [cases]
  (group-by (fn [[case-name alg]]
              (m/extract
               (alg-switches-parity-at-layer-default-positions? alg)))
            cases))

(defn algset-generates-parity-case?
  "For an algorithm that is known to start and end at square square, returns an
  Either[keyword] describing whether the algorithm has caused the puzzle to
  switch parity."
  [alg-set case]
  (let [created-puzzle (algset-scrambler/generate-puzzle alg-set case)]
    (m/mlet [odd-parity? (pps/puzzle-parity-at-default-layer-positions created-puzzle)]
            (m/return (if odd-parity?
                        :odd-parity-at-cubeshape
                        :even-parity-at-cubeshape)))))

(defn parity-and-non-parity-puzzles [alg-set algs]
  (group-by (fn [alg]
              (m/extract (algset-generates-parity-case? alg-set alg)))
            algs))
