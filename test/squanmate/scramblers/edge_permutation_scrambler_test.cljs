(ns squanmate.scramblers.edge-permutation-scrambler-test
  (:require [squanmate.scramblers.edge-permutation-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [cljs.test :as t :include-macros true])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/new-default-state))

(defcard-rg scramble-component
  [sut/scramble-component state]
  state
  {:inspect-data true})
