(ns squanmate.ui.scramble-test
  (:require [squanmate.ui.scramble :as sut]
            [clojure.test :refer [is] :include-macros true]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/default-state))

(defonce state-with-scramble (reagent/atom {:puzzle nil
                                            :scramble-algorithm "(3,2)/ (1,-5)/ (3,0)/"}))

(defcard-rg import-alg-view
  [sut/component state])

(defcard-rg import-alg-view-with-import-button
  [sut/component state-with-scramble])
