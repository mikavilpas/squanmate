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

(defn- alg-with-markers
  "Combines the parsed parts of the scramble algorithm with markers that will be
  attached to each specific algorithm token."
  [display-tokens markers]
  )

(defn- executio-steps [alg-string]
  (let [alg-steps (execution/transformations p/square-square alg-string)
        steps-either (eu/list-of-eithers->either-list alg-steps)]
    steps-either))

(defn alg-with-cubeshape-status-highlighted [alg-string]
  (m/mlet [display-tokens (parser/parse alg-string)
           execution-steps (scramble-steps alg-string)]
          (m/return
           (let [markers (cubeshape/entered-and-left-cubeshape execution-steps)]
             (alg-with-markers display-tokens markers)))))
