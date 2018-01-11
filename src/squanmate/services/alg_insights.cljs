(ns squanmate.services.alg-insights
  "Functions for analyzing an algorithm and finding out the points of it where
  interesting things happen. These things can be e.g. highlighted to the user."
  (:require [squanmate.shapes :as shapes]
            [cats.core :as m]
            [squanmate.alg.parser :as parser]
            [squanmate.utils.either-utils :as eu]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.services.alg-insights.cubeshape :as cubeshape]))

(defrecord Token [move markers])

(defn- combine-alg-with-markers
  "Combines the parsed parts of the scramble algorithm with markers that will be
  attached to each specific algorithm token.

  `markers-maps` should be a sequence of maps in the insights format. It must be
  as long as `display-tokens`.
  "
  [display-tokens markers-maps]
  (for [[index token] (zipmap (range) display-tokens)]
    (let [markers (->> markers-maps
                       (map #(get % index))
                       vec)]
      (->Token token markers))))

(defn- execution-steps [alg-string]
  (let [alg-steps (execution/transformations p/square-square alg-string)
        steps-either (eu/list-of-eithers->either-list alg-steps)]
    steps-either))

(defn alg-with-cubeshape-status-highlighted [alg-string]
  (m/mlet [display-tokens (parser/parse alg-string)
           steps (execution-steps alg-string)]
          (m/return
           (let [markers (cubeshape/entered-and-left-cubeshape steps)]
             (combine-alg-with-markers display-tokens [markers])))))
