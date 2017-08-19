(ns squanmate.ui.initial-rotation-adjuster-test
  (:require [squanmate.ui.initial-rotation-adjuster :as ira]
            [clojure.test :as t :refer [is]]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest possible-sliceable-rotations-test []
  (is (= {:positive [3]
          :negative [-3]}
         (ira/possible-sliceable-rotations shapes/parallel-edges))
      "returns only rotations that are sliceable")

  (is (= {:positive [1 3 4 6]
          :negative [-2 -3 -5 -6]}
         (ira/possible-sliceable-rotations shapes/square))
      "returns shorter rotations first"))
