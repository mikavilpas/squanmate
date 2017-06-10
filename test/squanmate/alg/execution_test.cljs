(ns squanmate.alg.execution-test
  (:require [squanmate.alg.execution :as e]
            [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as puzzle])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest can-rotate []
  (is (either/right? (e/execute (types/RotateTopLayer. 3)
                                puzzle/square-square))
      "top layer")

  (is (either/right? (e/execute (types/RotateBottomLayer. 3)
                                puzzle/square-square))
      "bottom layer"))

(deftest can-slice []
  (is (either/right? (e/execute (types/Slice.)
                                puzzle/square-square))))
