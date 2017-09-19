(ns squanmate.ui.parity-sequences-test
  (:require [squanmate.ui.parity-sequences :as sut]
            [clojure.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/default-state))

(defcard-rg ui
  [sut/parity-game state]
  state
  {:inspect-data true})

(deftest has-parity?-test []
  (is (true? (sut/has-parity? "BRG")))
  (is (false? (sut/has-parity? "BGR"))))
