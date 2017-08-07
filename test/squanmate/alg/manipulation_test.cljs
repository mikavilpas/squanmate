(ns squanmate.alg.manipulation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types])
  (:require-macros
   [devcards.core :as dc :refer [defcard defcard-rg deftest]]))

(deftest combine-rotations-test []
  (is (= (types/->Rotations -5 0)
         (manipulation/combine-rotations (types/->Rotations 6 0)
                                         (types/->Rotations 1 0)))
      "overflows on top layer")

  (is (= (types/->Rotations 4 0)
         (manipulation/combine-rotations (types/->Rotations -6 0)
                                         (types/->Rotations -2 0)))
      "underflows on top layer")

  (is (= (types/->Rotations -6 0)
         (manipulation/combine-rotations (types/->Rotations -4 0)
                                         (types/->Rotations -2 0)))
      "happy path"))
