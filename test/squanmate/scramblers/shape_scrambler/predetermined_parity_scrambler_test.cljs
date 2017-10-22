(ns squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

;; adjacent parity algorithm
(def parity-alg-applied (-> p/square-square
                            (execution/transformation-result
                             "/-3,0/0,3/0,-3/0,3/ 2,0/0,2/-2,0/4,0 /0,-2/ 0,2/-1,4/0,-3/*")
                            execution/puzzle-of-result))

(deftest puzzle-parity-at-default-layer-positions-test []
  "the main takeaway from this test is that the two parity counts should be the
  opposite"
  (is (false? (sut/puzzle-parity-at-default-layer-positions
               p/square-square)))

  (is (true? (sut/puzzle-parity-at-default-layer-positions
              parity-alg-applied))))

(deftest scrambler-test []
  (is (let [s (sut/->PredeterminedParityScrambler p/square-square
                                                  :same-relative-parity)]
        (scrambler/create-scramble s))
      "creates puzzle with same parity")

  (is (let [s (sut/->PredeterminedParityScrambler p/square-square
                                                  :opposite-relative-parity)]
        (scrambler/create-scramble s))
      "creates with opposite parity"))

(deftest reorient-to-default-layer-positions-test []
  (is (shapes/same-shape-and-orientation?
       p/square-square
       (sut/reorient-to-default-layer-positions p/square-square)))

  (is (shapes/same-shape-and-orientation?
       p/square-square
       (sut/reorient-to-default-layer-positions parity-alg-applied)))

  (is (shapes/same-shape-and-orientation?
       p/square-square
       (sut/reorient-to-default-layer-positions (-> p/square-square
                                                    (execution/transformation-result "-2,-1")
                                                    execution/puzzle-of-result)))))
