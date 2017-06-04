(ns squanmate.puzzle-test
  (:require [squanmate.puzzle :as p]
            [clojure.test :as t :refer [is]]
            [cats.monad.either :as either])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest piece-value-test []
  (is (= 2 (p/piece-value p/edge)))
  (is (= 1 (p/piece-value p/corner))))

(deftest square-square-pieces-str-test []
  (is (= "cececece" (p/pieces-str (:top-layer p/square-square))))
  (is (= "ecececec" (p/pieces-str (:bottom-layer p/square-square)))))
