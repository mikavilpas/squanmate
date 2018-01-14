(ns squanmate.ui.alg-visualizer-test
  (:require [squanmate.ui.alg-visualizer :as v]
            [clojure.test :as t]
            [squanmate.alg.puzzle :as p]
            [squanmate.alg.puzzle :as puzzle]
            [reagent.core :as reagent]
            [cljs.test :refer [is]]
            [squanmate.alg.execution :as execution])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest ends-at-cubeshape?-test []
  (is (true? (-> puzzle/square-square
                 (execution/transformations "1/3")
                 v/ends-at-cubeshape?)))

  (is (false? (-> puzzle/square-square
                  (execution/transformations "/3")
                  v/ends-at-cubeshape?))))

(defcard-rg turn-and-slice
  [v/algorithm-visualization
   puzzle/square-square
   "3/"])

(defcard-rg square-square-alg
  [:div
   "this is the lin method algorithm for the CP+DF opposite corners case"
   [v/algorithm-visualization
    puzzle/square-square
    "1,0 / -4,-3 / -3,0 / -3,-3 / -3,0 / -2,-3 /"]])

(defonce alg-visualizer-state (v/default-alg-visualizer-state))

(defcard-rg alg-visualizer
  [v/alg-visualizer alg-visualizer-state]
  alg-visualizer-state
  {:inspect-data true})
