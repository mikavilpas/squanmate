(ns squanmate.scramblers.alg-trainer.scramble-generation-test
  (:require [squanmate.scramblers.alg-trainer.scramble-generation :as sut]
            [cljs.test :as t :refer [is]]
            [reagent.core :as reagent]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.puzzle :as p])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))


(defonce state (reagent/atom {}))

(defcard-rg alal
  [:div
   (sut/new-scramble state)]
  state
  {:inspect-data true})
