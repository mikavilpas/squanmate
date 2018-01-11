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
  (is (= {0 sut/in-cubeshape}
         (sut/entered-and-left-cubeshape (execute "1/-1")))
      "starting step, rotations, slice, rotations")

  (is (= {0 sut/in-cubeshape
          1 sut/shape-shifted}
         (sut/entered-and-left-cubeshape (execute "/6/")))))
