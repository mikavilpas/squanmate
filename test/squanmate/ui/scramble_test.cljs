(ns squanmate.ui.scramble-test
  (:require [squanmate.ui.scramble :as sut]
            [clojure.test :refer [is] :include-macros true]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def state-without-scramble
  (sut/default-state))

(def state-with-scramble
  (let [a (sut/default-state)]
    (swap! a assoc-in [:scramble :scramble-algorithm] "(3,2)/ (1,-5)/ (3,0)/")
    a))

(defcard-rg import-alg-view
  [sut/component state-without-scramble])

(defcard-rg import-alg-view-with-import-button
  [:div [:p "The import happens in two stages:"]
   [:p "First the user enters the alg, then clicks Import. This is to make it
    clear when a scramble is invalid."]
   [sut/component state-with-scramble]])

(def state-with-imported-alg
  (let [a (sut/default-state)]
    (swap! a #(-> %
                  (assoc-in [:scramble :scramble-algorithm]
                            "(3,2)/ (-2,4)/ (-4,-1)/ (1,-5)/ (2,-1)/ (4,-5)/
                             (3,-3)/ (-1,-4)/ (-3,-5)/ (0,-3)/ (4,-1)/ (0,-4)/
                             (6,-5)/ (-2,0)")
                  (assoc :imported? true)))
    a))

(defcard-rg alg-imported
  [sut/component state-with-imported-alg]
  state-with-imported-alg
  {:inspect-data true})
