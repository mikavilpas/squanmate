(ns squanmate.utils.alg-verification-utils
  "Tools for verifying the validity of algorithms."
  (:require [squanmate.alg.parser :as parser]
            [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [cats.core :as m]
            [squanmate.services.cube-aligner :as cube-aligner]
            [squanmate.alg.types :as types]))

(defn- parse [[case-name alg]]
  [case-name (parser/parse alg)])

(defn non-parseable-cases [cases]
  (let [parse-results (map parse cases)]
    (filter (fn [[case-name result]]
              (either/left? result))
            parse-results)))

(defn- execute-reverse [[case-name alg]]
  [case-name
   (execution/transformation-result-reverse p/square-square
                                            alg)])

(defn non-executable-cases [cases]
  (let [execution-results (map execute-reverse cases)]
    (->> execution-results
         (filter (fn [[case-name result]]
                   (either/left? result))))))

(defn- rotations-for-alg [alg]
  (m/mlet [end-step (execution/transformation-result p/square-square
                                                     alg)
           rotations (cube-aligner/rotations-to-align-cube (:puzzle end-step))]
          rotations))

(defn non-aligned-cases [cases]
  (for [[case-name alg] cases
        :let [rotations (rotations-for-alg alg)]
        :when (not (= rotations (types/Rotations. 0 0)))]
    [case-name rotations]))
