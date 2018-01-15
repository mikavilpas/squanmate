(ns squanmate.services.count-positions-test
  (:require [squanmate.alg.puzzle :as p]
            [squanmate.services.count-positions :as count-positions]
            [cljs.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.services.shapes :as shapes]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest count-positions-test []
  (is (= #{#{0 3 -3 6} #{1 -2 4 -5}}
         (count-positions/count-positions shapes/square)))

  (is (= #{#{0 3 -3 6} #{2 -2 4 -4}}
         (count-positions/count-positions shapes/barrel)))

  (is (= #{#{0 6}
           #{1 -5}}
         (count-positions/count-positions shapes/mushroom))))

(defcard-rg mushroom
  [newmonochrome/layer-component shapes/mushroom])
