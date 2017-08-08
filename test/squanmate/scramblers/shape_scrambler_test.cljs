(ns squanmate.scramblers.shape-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [reagent.core :as reagent]
            [squanmate.ui.common :as common])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defonce scramble-state (reagent/atom (shape-scrambler/scramble)))

(defn new-scramble! []
  (reset! scramble-state (shape-scrambler/scramble)))

;; todo a scramble like (0,-4)/ (-3,0)/ (3,0)/ (-5,-2)/ (5,-1)/ (-3,0)/ (3,0)/
;; (-2,-5)/ (-3,-1)/ (0,-3)/ (5,-2)/ (0,-2)/ (1,-4)/ (-3,3) will leave the
;; bottom layer in an impossible position!

(defcard-rg scramble-preview
  [:div
   [shape-scrambler/scramble-component @scramble-state]
   [:div.center
    [common/button {:on-click new-scramble!} "New scramble"]]])
