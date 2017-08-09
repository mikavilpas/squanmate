(ns squanmate.scramblers.shape-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [reagent.core :as reagent]
            [squanmate.ui.common :as common])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defonce scramble-state (reagent/atom (shape-scrambler/scramble)))

(defn new-scramble! []
  (reset! scramble-state (shape-scrambler/scramble)))

(defcard-rg scramble-preview
  [:div
   [shape-scrambler/scramble-component @scramble-state]
   [:div.center
    [common/button {:on-click new-scramble!} "New scramble"]]])
