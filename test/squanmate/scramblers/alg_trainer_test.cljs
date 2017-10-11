(ns squanmate.scramblers.alg-trainer-test
  (:require [squanmate.scramblers.alg-trainer :as sut]
            [clojure.test :as t :refer [is]]
            [cljs.test :as t :include-macros true]
            [squanmate.puzzle :as p])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/new-default-state))

(defcard-rg scramble-component-no-algs
  [sut/trainer-component state]
  state
  {:inspect-data true})
