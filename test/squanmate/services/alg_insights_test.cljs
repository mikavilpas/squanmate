(ns squanmate.services.alg-insights-test
  (:require [cats.core :as m]
            [cljs.test :refer [is]]
            [squanmate.alg.types :as types]
            [squanmate.services.alg-insights.types :as t]
            [squanmate.services.alg-insights :as sut]
            [squanmate.services.alg-insights.cubeshape :as cs])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def slice (types/->Slice))
(defn rotations [top bottom]
  (types/->Rotations top bottom))

(def token t/map->Token)

(deftest alg-with-cubeshape-status-highlighted-test []
  (is (= [(token {:move slice, :markers [(cs/->InCubeshape)]})
          (token {:move (rotations 6 0), :markers [(cs/->ShapeShifted)]})
          (token {:move slice, :markers [(cs/->ShapeShifted)]})
          (token {:move (rotations 6 0), :markers [(cs/->ShapeShifted)]})
          (token {:move slice, :markers [(cs/->ShapeShifted)]})]
         (m/extract
          (sut/alg-with-cubeshape-status-highlighted "/6/6/")))
      "returns a sequence of algorithm moves and their associated insight markers"))
