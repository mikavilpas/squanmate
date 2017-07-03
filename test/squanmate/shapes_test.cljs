(ns squanmate.shapes-test
  (:require [squanmate.shapes :as shapes]
            [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as r])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest ordered-permutations-test []
  (is (= [[1 2 3]
          [2 3 1]
          [3 1 2]]
         (shapes/ordered-permutations [1 2 3]))))

(deftest same-piece-type-order?-test []
  (is (true? (shapes/same-piece-type-order? shapes/square
                                            shapes/square))))

(deftest layer-shape-name-test []
  (is (= "Square"
         (shapes/layer-shape-name shapes/square))))
