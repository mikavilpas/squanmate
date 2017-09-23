(ns squanmate.ui.rotation-adjuster-controls-test
  (:require [squanmate.ui.rotation-adjuster-controls :as rac]
            [clojure.test :as t :refer [is]]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest possible-sliceable-rotations-test []
  (is (= {:positive [6]
          :negative []}
         (rac/possible-sliceable-rotations shapes/parallel-edges))
      "returns only rotations that are sliceable")

  (is (= {:positive [1 3 4 6]
          :negative [-2 -3 -5]}
         (rac/possible-sliceable-rotations shapes/square))
      "returns shorter rotations first"))
