(ns squanmate.ui.alg-visualizer
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [clojure.string :as string]
            [squanmate.alg.parser :as parser]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [cats.monad.either :as either]))

(defn algorithm-visualization [puzzle alg-string]
  (let [steps (execution/transformations puzzle alg-string)]
    [:div
     (for [step steps]
       (either/branch-right step monochrome/monochrome-puzzle))]))
