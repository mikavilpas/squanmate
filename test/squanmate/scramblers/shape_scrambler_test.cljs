(ns squanmate.scramblers.shape-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [reagent.core :as reagent]
            [squanmate.ui.common :as common])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defonce scramble-state (shape-scrambler/new-state))

(defcard-rg scramble-preview
  [shape-scrambler/scramble-component scramble-state]
  scramble-state
  {:inspect-data true})
