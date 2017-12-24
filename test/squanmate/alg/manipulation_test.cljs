(ns squanmate.alg.manipulation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types])
  (:require-macros
   [devcards.core :as dc :refer [defcard defcard-rg deftest]]))

(defn- rotate [top bottom]
  (types/->Rotations top bottom))

(def slice (types/->Slice))

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

(deftest flip-alg-upside-down-test []
  (is (= [(rotate 0 1)]
         (manipulation/flip-alg-upside-down [(rotate 1 0)])))

  (is (= [(rotate 0 -1)
          (rotate 5 -3)]
         (manipulation/flip-alg-upside-down [(rotate -1 0)
                                             (rotate -3 5)]))))

(deftest append-final-rotation-test []
  (is (= [(rotate 1 2)]
         (manipulation/append-final-rotation (rotate 1 2)
                                             []))
      "can append to empty steps")

  (is (= [(rotate 5 5)
          slice
          (rotate 2 3)]
         (manipulation/append-final-rotation (rotate 1 2)
                                             [(rotate 5 5)
                                              slice
                                              (rotate 1 1)]))
      "can combine last rotations")

  (is (= [(rotate 5 5)
          slice
          (rotate 1 2)]
         (manipulation/append-final-rotation (rotate 1 2)
                                             [(rotate 5 5)
                                              slice]))
      "can append in case the last step is a slice")

  (is (= []
         (manipulation/append-final-rotation (rotate 0 0)
                                             []))
      "ignore empty rotation"))

(deftest prepend-initial-rotation-test []
  (is (= [(rotate 1 2)]
         (manipulation/prepend-initial-rotation (rotate 1 2)
                                                []))
      "append to empty steps")

  (is (= []
         (manipulation/prepend-initial-rotation (rotate 0 0)
                                                []))
      "ignore empty rotation"))
