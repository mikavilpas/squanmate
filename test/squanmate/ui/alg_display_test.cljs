(ns squanmate.ui.alg-display-test
  (:require [squanmate.ui.alg-display :as sut])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg rich-scramble-display
  [:div
   [sut/rich-scramble-display "1/-1"]
   [sut/rich-scramble-display "/6/6/"]])
