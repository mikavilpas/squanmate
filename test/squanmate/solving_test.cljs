(ns squanmate.solving-test
  (:require [squanmate.solving :as solving]
            [clojure.test :as t :refer [is async]]
            [cljs.test :as t :include-macros true]
            [cljs.core.async :refer [<! timeout]]
            [squanmate.puzzle :as puzzle]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes]
            [cats.monad.either :as either])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]
   [cljs.core.async.macros :as m :refer [go]]))

(deftest convert-to-state-string-test []
  (is (= "A1B2C3D45E6F7G8H"
         (solving/convert-to-state-string puzzle/square-square))
      "should be able to convert square square to solver notation"))

(deftest worker-poc-test []
  (async done
         (go
           (let [result-atom (solving/solve-state "A2B3C1D45E6F7G8H")]
             (<! (timeout 500))
             (is (= "( 0, 2)/(-5, 4)/( 5, 2)/(-3, 0)/( 0, 3)/(-5, 1)/(-1, 2)/(-3,-2)"
                    @result-atom)
                 "solve an example from the readme file of Jaap's solver")
             (done)))))

(defn scrambled-puzzle [alg]
  (let [result (execution/transformation-result puzzle/square-square alg)]
    (assert (either/right? result))
    (:puzzle (deref result))))

(defn should-be-solved-with [scramble-alg expected-solve-alg done]
  (go
    (let [result-atom (solving/solve (scrambled-puzzle scramble-alg))]
      (<! (timeout 500))
      (is (= expected-solve-alg @result-atom)
          (str "should solve " scramble-alg " with " expected-solve-alg))
      (done))))

(deftest solve-kite-kite []
  (async done
         (should-be-solved-with "/3" "(-3, 0)/" done)))

(deftest solve-lin-back-corners []
  (async done
         (should-be-solved-with
          "4,-3 / -3,0 / -1,2 / 1,-2 / -3,3 / -3,0 /"
          "/( 3, 0)/(-3, 3)/(-4, 5)/(-2, 1)/( 6,-3)/( 2,-3)"
          done)))

;; todo test that a non-symmetrical position can be solved
