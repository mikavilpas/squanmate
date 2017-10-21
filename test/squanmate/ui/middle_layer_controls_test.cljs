(ns squanmate.ui.middle-layer-controls-test
  (:require [squanmate.ui.middle-layer-controls :as sut]
            [clojure.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/default-state))

(defcard-rg lul
  [sut/controls state]
  state
  {:inspect-data true})
