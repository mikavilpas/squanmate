(ns squanmate.services.alg-insights-test
  (:require [cats.core :as m]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.types :as types]
            [squanmate.services.alg-insights :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def slice (types/->Slice))
(defn rotations [top bottom]
  (types/->Rotations top bottom))

(def token sut/map->Token)

(deftest alg-with-cubeshape-status-highlighted-test []
  (is (= [(token {:move slice, :markers [:in-cubeshape]})
          (token {:move (rotations 6 0), :markers [:shape-shifted]})
          (token {:move slice, :markers [:shape-shifted]})
          (token {:move (rotations 6 0), :markers [:shape-shifted]})
          (token {:move slice, :markers [:shape-shifted]})]
         (m/extract
          (sut/alg-with-cubeshape-status-highlighted "/6/6/")))
      "returns a sequence of algorithm moves and their associated insight markers"))
