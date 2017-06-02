(ns squanmate.puzzle-test
  (:require [squanmate.puzzle :as p]
            [clojure.test :as t :refer [is]]
            [cats.monad.either :as either])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest piece-value-test []
  (is (= 2 (p/piece-value p/edge)))
  (is (= 1 (p/piece-value p/corner))))

(deftest layer-sliceable?-test []
  (is (true? (p/layer-sliceable? p/square-layer))))

(deftest rotate-layer-clockwise-test []
  (is (= (p/rotate-layer p/square-layer 0)
         (either/right p/square-layer)))

  ;; rotating in 90 degree increments must preserve the square shape
  (let [expected-square (either/right p/square-layer)]
    (is (= (p/rotate-layer p/square-layer 3) expected-square)))

  ;; rotating by other amounts
  (let [e p/edge
        c p/corner]
    (is (= (p/rotate-layer p/square-layer 1)
           (either/right (p/Layer. [e c e c
                                    e c e c]))))
    (is (= (p/rotate-layer p/square-layer 4)
           (either/right (p/Layer. [e c e c
                                    e c e c]))))))
