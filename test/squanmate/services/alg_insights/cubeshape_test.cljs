(ns squanmate.services.alg-insights.cubeshape-test
  (:require [cats.core :as m]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.puzzle :as p]
            [squanmate.services.alg-insights.cubeshape :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- execute [alg-string]
  (let [step-result-eithers (execution/transformations p/square-square alg-string)]
    (map m/extract step-result-eithers)))

(def in-cubeshape (sut/->InCubeshape))
(def shape-shifted (sut/->ShapeShifted))

(deftest entered-and-left-cubeshape-test []
  (is (= {0 in-cubeshape
          1 in-cubeshape
          2 in-cubeshape
          3 in-cubeshape}
         (sut/entered-and-left-cubeshape (execute "1/-1")))
      "starting step, rotations, slice, rotations")

  (is (= {0 in-cubeshape
          1 shape-shifted
          2 shape-shifted
          3 shape-shifted
          4 shape-shifted
          5 in-cubeshape}
         (sut/entered-and-left-cubeshape (execute "/6/6/")))
      "This is the common alg that fixes the middle layer. It goes to kite kite
      and returns to cubeshape"))
