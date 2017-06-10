(ns squanmate.ui.alg-visualizer-test
  (:require [squanmate.ui.alg-visualizer :as v]
            [clojure.test :as t]
            [squanmate.puzzle :as p]
            [squanmate.puzzle :as puzzle])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg LUL
  [v/algorithm-visualization puzzle/square-square "3/"])
