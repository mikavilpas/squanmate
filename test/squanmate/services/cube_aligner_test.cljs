(ns squanmate.services.cube-aligner-test
  (:require [cats.monad.either :as either]
            [cljs.test :as t :refer [is]]
            [squanmate.services.cube-aligner :as sut]
            [squanmate.services.shapes :as shapes]
            [squanmate.alg.types :as types]
            [squanmate.alg.rotation :as rotation]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [cats.core :as m])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- rotate-solved-puzzle [top bottom]
  (-> (execution/transformation-result p/square-square
                                       (str top "," bottom))
      m/extract
      :puzzle))

(deftest align-cube-test []
  (is (either/left?
       (sut/rotations-to-align-cube (shapes/puzzle-with-layers "kite" "mushroom")))
      "should not work if not in cubeshape")

  (is (= (either/right (types/Rotations. 0 1))
         (sut/rotations-to-align-cube (shapes/puzzle-with-layers "square" "square"))))

  (is (= (either/right (types/Rotations. -1 0))
         (sut/rotations-to-align-cube (rotate-solved-puzzle 1 0))))

  (is (= (either/right (types/Rotations. -1 0))
         (sut/rotations-to-align-cube (rotate-solved-puzzle 4 0))))

  (is (= (either/right (types/Rotations. -1 1))
         (sut/rotations-to-align-cube (rotate-solved-puzzle -2 2)))))
