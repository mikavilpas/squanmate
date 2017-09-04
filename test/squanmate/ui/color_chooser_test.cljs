(ns squanmate.ui.color-chooser-test
  (:require [squanmate.ui.color-chooser :as color-chooser]
            [clojure.test :as t])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (color-chooser/default-color-chooser-state))

(defcard-rg color-chooser
  [color-chooser/color-chooser state]
  state
  {:inspect-data true})
