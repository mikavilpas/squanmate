(ns squanmate.alg.execution-test
  (:require [squanmate.alg.execution :as e]
            [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as puzzle]
            [cats.core :as m]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard defcard-rg deftest]]))

(deftest rotate-top []
  "top layer"
  (let [rotation-result (e/execute (types/Rotations. 3 0)
                                   puzzle/square-square)]
    (is (either/right? rotation-result))
    ;; the rest of the test is a hack with mlet. if rotation-result were a Left,
    ;; the rest of the test would not get rendered. This is why it's important
    ;; to have (is (either/right? rotation-result)) above.
    (m/mlet [result rotation-result]
            (is (= (types/Rotations. 3 0)
                   (:previously-applied-step result))))))

(deftest rotate-bottom []
  (let [rotation-result (e/execute (types/Rotations. 0 3)
                                   puzzle/square-square)]
    (is (either/right? rotation-result))
    (m/mlet [result rotation-result]
            (is (= (types/Rotations. 0 3)
                   (:previously-applied-step result))))))

(deftest can-slice []
  (let [rotation-result (e/execute (types/Slice.)
                                   puzzle/square-square)]
    (is (either/right? rotation-result))
    (m/mlet [result rotation-result]
            (is (= (types/Slice.)
                   (:previously-applied-step result))))))

(deftest transformations-test []
  "test turning 3/"
  (let [result (e/transformations puzzle/square-square "3/")]
    (is (= (count result)
           (count (either/rights result)))
        "all results should be Right in this case")))

(defcard-rg inspect-result
  [:div
   (e/transformations puzzle/square-square "3/")])
