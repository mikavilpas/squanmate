(ns squanmate.ui.color-chooser-test
  (:require [squanmate.ui.color-chooser :as color-chooser]
            [clojure.test :as t]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce color-picker-state (reagent/atom {:color :green}))

(defcard-rg color-picker
  [color-chooser/custom-color-chooser (reagent/cursor color-picker-state [:color])]
  color-picker-state
  {:inspect-data true})

(defonce state (color-chooser/default-color-chooser-state))

(defcard-rg color-chooser
  [color-chooser/color-chooser state]
  state
  {:inspect-data true})
