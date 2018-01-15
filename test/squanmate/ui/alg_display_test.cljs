(ns squanmate.ui.alg-display-test
  (:require [squanmate.ui.alg-display :as sut])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg rich-scramble-display
  [:div
   [sut/rich-scramble-display "1/-1"]
   [sut/rich-scramble-display "/6/6/"]

   ;; an alg that sets up parity and returns to square square
   [sut/rich-scramble-display "(3,-4)/ (-3,6)/ (0,-3)/ (4,-5)/ (2,-1)/ (-3,0)/ (4,-2)/ (5,0)/ (-3,0)/ (4,-1)/ (2,-2)/ (4,-4)/ (4,-1)/ (0,-3)/ (0,-2)"]])
