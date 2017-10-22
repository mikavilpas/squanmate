(ns squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.alg.execution :as execution])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

;; adjacent parity algorithm
(def parity-alg-applied (-> p/square-square
                            (execution/transformation-result
                             "/-3,0/0,3/0,-3/0,3/ 2,0/0,2/-2,0/4,0 /0,-2/ 0,2/-1,4/0,-3/*")
                            m/extract
                            :puzzle))

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
        (scrambler/create-scramble s))))
