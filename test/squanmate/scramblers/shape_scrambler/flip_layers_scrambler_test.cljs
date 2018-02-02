(ns squanmate.scramblers.shape-scrambler.flip-layers-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler.flip-layers-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.alg.puzzle :as p]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.alg.execution :as execution]
            [squanmate.services.shapes :as shapes])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defn- flip [top-shape bottom-shape]
  (let [puzzle (shapes/puzzle-with-layers top-shape bottom-shape)
        flipper (sut/->FlipLayersScrambler puzzle)]
    (scrambler/create-scramble flipper)))

(defn- names [[names puzzle]]
  names)

(deftest flip-layers-test []
  (is (= ["kite" "barrel"]
         (names (flip "barrel" "kite"))))
  (is (= ["barrel" "right-fist"]
         (names (flip "right-fist" "barrel")))))
