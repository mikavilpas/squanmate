(ns squanmate.alg.manipulation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types])
  (:require-macros
   [devcards.core :as dc :refer [defcard defcard-rg deftest]]))

(defn- rotate [top bottom]
  (types/->Rotations top bottom))

(deftest combine-rotations-test []
  (is (= (rotate -5 0)
         (manipulation/combine-rotations (rotate 6 0)
                                         (rotate 1 0)))
      "overflows on top layer")

  (is (= (rotate 4 0)
         (manipulation/combine-rotations (rotate -6 0)
                                         (rotate -2 0)))
      "underflows on top layer")

  (is (= (rotate 6 0)
         (manipulation/combine-rotations (rotate -6 0)
                                         (rotate 0 0)))
      "6 is the same as -6, except more simple")

  (is (= (rotate 6 0)
         (manipulation/combine-rotations (rotate -4 0)
                                         (rotate -2 0)))
      "happy path"))
