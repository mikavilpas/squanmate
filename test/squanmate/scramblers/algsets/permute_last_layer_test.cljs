(ns squanmate.scramblers.algsets.permute-last-layer-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.permute-last-layer :as pll]
            [squanmate.utils.alg-verification-utils :as av]
            [cats.monad.either :as either]
            [squanmate.scramblers.algsets.algset :as algset])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest parse-all-cases-test []
  (is (empty? (av/non-parseable-cases pll/all-cases))))

(deftest execute-all-cases-test []
  (is (empty? (av/non-executable-cases pll/all-cases))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases pll/all-cases))))

(deftest parity-of-cases []
  (let [results (av/parity-and-non-parity-cases pll/all-cases)
        odd-cases (get results :switches-parity)
        even-cases (get results :preserves-parity)]
    (is (= (:even-cases pll/pll-algset)
           even-cases))
    (is (= (:odd-cases pll/pll-algset)
           odd-cases))))
