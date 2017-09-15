(ns squanmate.services.count-positions-test
  (:require [squanmate.puzzle :as p]
            [squanmate.services.count-positions :as count-positions]
            [cljs.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest count-positions-test []
  (is (= #{#{0 3 -3 6} #{1 -2 4 -5}}
         (count-positions/count-positions shapes/square))))
