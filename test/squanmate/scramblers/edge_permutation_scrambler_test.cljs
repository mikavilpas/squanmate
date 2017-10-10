(ns squanmate.scramblers.edge-permutation-scrambler-test
  (:require [squanmate.scramblers.edge-permutation-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.scramblers.edge-permutation-scrambler.actions :as actions]
            [cljs.test :as t :include-macros true]
            [squanmate.puzzle :as p])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/new-default-state))

(defcard-rg scramble-component
  [sut/scramble-component state]
  state
  {:inspect-data true})

(defn puzzle-corners [puzzle]
  (->> puzzle
       p/puzzle-pieces
       (filterv p/corner?)))

(def solved-corners (puzzle-corners p/square-square))

(deftest randomize-edges-test []
  (is (= solved-corners
         (puzzle-corners (actions/new-scramble)))
      "should have solved corners"))
