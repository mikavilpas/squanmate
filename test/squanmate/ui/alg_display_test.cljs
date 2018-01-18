(ns squanmate.ui.alg-display-test
  (:require [squanmate.ui.alg-display :as sut])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg rich-scramble-display
  [:div
   [sut/rich-scramble-display "1/-1"]
   [sut/rich-scramble-display "/6/6/"]

   [:div
    "an alg that sets up parity and returns to square square"
    [sut/rich-scramble-display "(3,-4)/ (-3,6)/ (0,-3)/ (4,-5)/ (2,-1)/ (-3,0)/ (4,-2)/ (5,0)/ (-3,0)/ (4,-1)/ (2,-2)/ (4,-4)/ (4,-1)/ (0,-3)/ (0,-2)"]]

   [:div
    "leave misaligned and return aligned"
    [sut/rich-scramble-display "1,-1/6,6/"]]

   [:div
    "leave misaligned and enter back misaligned"
    [sut/rich-scramble-display "1,-1/0/"]]

   [:div
    "a complicated case that would render the last few moves after the first move"
    [sut/rich-scramble-display "(-5,0)/ (-4,2)/ (-5,-2)/ (-4,-1)/ (0,-3)/ (1,-5)/ (-1,-4)/ (-3,-3)/ (0,-3)/ (0,-3)/ (0,-2)/ (-3,0)/ (4,-1)/ (2,-2)/ (4,-4)/ (4,-1)/ (0,-3)/"]]
   ])
