(ns squanmate.solving-test
  (:require [squanmate.solving :as solving]
            [clojure.test :as t :refer [is async]]
            [cljs.test :as t :include-macros true]
            [cljs.core.async :refer [<! timeout]]
            [squanmate.puzzle :as puzzle])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]
   [cljs.core.async.macros :as m :refer [go]]))

(deftest worker-poc-test []
  (async done
         (go
           (let [result-atom (solving/solve "A2B3C1D45E6F7G8H")]
             (<! (timeout 500))
             (is (= "( 0, 2)/(-5, 4)/( 5, 2)/(-3, 0)/( 0, 3)/(-5, 1)/(-1, 2)/(-3,-2)"
                    @result-atom))
             (done)))))

(deftest convert-to-state-string-test []
  (is (= "A1B2C3D45E6F7G8H"
         (solving/convert-to-state-string puzzle/square-square))
      "should be able to convert square square to solver notation"))
