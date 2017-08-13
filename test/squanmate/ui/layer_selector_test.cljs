(ns squanmate.ui.layer-selector-test
  (:require [squanmate.ui.layer-selector :as layer-selector]
            [reagent.core :as reagent]
            [cljs.test :refer [is]]
            [squanmate.ui.common :as common])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(deftest filtered-shape-selections-test []
  (is (= #{"seven-one" "eight" "five-three" "four-four" "six-two"}
         (layer-selector/filtered-possible-shapes "star"))
      "must not include name of layer-filter in possible selections when it's not
      possible to construct such a puzzle")

  (is (= #{"scallop" "left-fist" "right-pawn" "barrel" "mushroom" "kite"
           "square" "left-pawn" "shield" "right-fist"}
         (layer-selector/filtered-possible-shapes "square"))
      "must include layer-filter in possible selections when a puzzle can be
      constructed from two of the same shape"))
