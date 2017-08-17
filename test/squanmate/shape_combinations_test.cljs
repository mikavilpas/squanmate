(ns squanmate.shape-combinations-test
  (:require [squanmate.shape-combinations :as shape-combinations]
            [clojure.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defn- includes? [coll v]
  (some #{v} coll))

(deftest possible-layers-test
  (is (= 90 (count shape-combinations/possible-layers)))
  (is (includes? shape-combinations/possible-layers ["square" "square"])))

(deftest filtered-shape-selections-test []
  (is (= #{"seven-one" "eight" "five-three" "four-four" "six-two"}
         (shape-combinations/filtered-possible-shapes "star"))
      "must not include name of layer-filter in possible selections when it's not
      possible to construct such a puzzle")

  (is (= #{"scallop" "left-fist" "right-pawn" "barrel" "mushroom" "kite"
           "square" "left-pawn" "shield" "right-fist"}
         (shape-combinations/filtered-possible-shapes "square"))
      "must include layer-filter in possible selections when a puzzle can be
      constructed from two of the same shape"))
