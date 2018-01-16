(ns squanmate.scramblers.algsets.permute-last-layer-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.permute-last-layer :as pll]
            [squanmate.utils.alg-verification-utils :as av]
            [cats.monad.either :as either]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def all-cases (algset-scrambler/all-cases pll/pll-algset))

(deftest parse-all-cases-test []
  (is (empty? (av/non-parseable-cases all-cases))))

(deftest execute-all-cases-test []
  (is (empty? (av/non-executable-cases all-cases))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases all-cases))))

(deftest parity-of-cases []
  "This tests that all of the algset's reported even and odd algs are actually
  even or odd respectively."
  (let [results (av/parity-and-non-parity-cases all-cases)
        odd-cases (get results :switches-parity)
        even-cases (get results :preserves-parity)]
    (is (= (:even-cases pll/pll-algset)
           even-cases))
    (is (= (:odd-cases pll/pll-algset)
           odd-cases))))
