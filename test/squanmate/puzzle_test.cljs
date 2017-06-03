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
