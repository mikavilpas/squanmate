(ns squanmate.ui.inspection-timer-test
  (:require [squanmate.ui.inspection-timer :as sut])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg inspection-timer
  [sut/inspection-timer 15])
