(ns squanmate.services.alg-insights
  "Functions for analyzing an algorithm and finding out the points of it where
  interesting things happen. These things can be e.g. highlighted to the user or
  used as part of some other computation."
  (:require [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.puzzle :as p]
            [squanmate.services.alg-insights.alignment :as alignment]
            [squanmate.services.alg-insights.cubeshape :as cubeshape]
            [squanmate.services.alg-insights.types :as types]
            [squanmate.utils.either-utils :as eu]))

(defn- add-indices-and-preserve-order [display-tokens]
  (->> display-tokens
       (interleave (range))
       (partition 2)))

(defn- combine-alg-with-markers
  "Combines the parsed parts of the scramble algorithm with markers that will be
  attached to each specific algorithm token.

  `markers-maps` should be a sequence of maps in the insights format.
  "
  [display-tokens markers-maps]
  (for [[index token] (add-indices-and-preserve-order display-tokens)]
    (let [markers (->> markers-maps
                       (map #(get % index))
                       vec
                       (filter #(not (nil? %))))]
      (types/->Token token markers))))

(defn- execution-steps [alg-string]
  (let [alg-steps (execution/transformations p/square-square alg-string)
        steps-either (eu/list-of-eithers->either-list alg-steps)]
    steps-either))

(defn alg-with-cubeshape-status-highlighted [alg-string]
  (m/mlet [display-tokens (parser/parse alg-string)
           steps (execution-steps alg-string)]
          (m/return
           (combine-alg-with-markers
            display-tokens
            (let [cubeshape-status (cubeshape/entered-and-left-cubeshape steps)
                  leaving-or-entering (alignment/alignments-when-entering-or-leaving-cubeshape steps)]
              [cubeshape-status
               leaving-or-entering])))))
