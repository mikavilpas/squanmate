(ns squanmate.scramblers.algsets.permute-last-layer-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.permute-last-layer :as pll]
            [squanmate.utils.alg-verification-utils :as av])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest parse-all-cases-test []
  (is (empty? (av/non-parseable-cases pll/all-cases-unordered))))

(deftest execute-all-cases-test []
  (is (empty? (av/non-executable-cases pll/all-cases-unordered))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases pll/all-cases-unordered))))
