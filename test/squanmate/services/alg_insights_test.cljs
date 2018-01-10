(ns squanmate.services.alg-insights-test
  (:require [cats.core :as m]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.services.alg-insights :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- execute [alg-string]
  (let [step-result-eithers (execution/transformations p/square-square alg-string)]
    (map m/extract step-result-eithers)))

(deftest entered-and-left-cubeshape-test []
  (is (= [(sut/->InCubeshape 4)]
         (sut/entered-and-left-cubeshape (execute "1/-1")))
      "starting step, rotations, slice, rotations")

  (is (= [(sut/->InCubeshape 1)
          (sut/->ShapeShifted 3)]
         (sut/entered-and-left-cubeshape (execute "/6/")))))
