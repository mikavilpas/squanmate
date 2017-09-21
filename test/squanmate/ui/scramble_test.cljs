(ns squanmate.ui.scramble-test
  (:require [squanmate.ui.scramble :as sut]
            [clojure.test :refer [is] :include-macros true]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def state-with-scramble
  (sut/default-state))

(def state-without-scramble
  (let [a (sut/default-state)]
    (swap! a assoc :scramble-algorithm "(3,2)/ (1,-5)/ (3,0)/")
    a))

(defcard-rg import-alg-view
  [sut/component state-without-scramble])

(defcard-rg import-alg-view-with-import-button
  [sut/component state-with-scramble])
