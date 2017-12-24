(ns squanmate.scramblers.algsets.edge-permutation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.utils.alg-verification-utils :as av]
            [squanmate.scramblers.algsets.algset :as algset])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def all-cases (algset/all-cases ep/ep-algset))

(deftest parse-all-ep-cases-test []
  (is (empty? (av/non-parseable-cases all-cases))))

(deftest execute-all-ep-cases-test []
  (is (empty? (av/non-executable-cases all-cases))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases all-cases))))

(deftest parity-of-cases []
  "This tests that all of the algset's reported even and odd algs are actually
  even or odd respectively."
  (let [results (av/parity-and-non-parity-cases all-cases)
        odd-cases (get results :switches-parity)
        even-cases (get results :preserves-parity)]
    (is (= (:even-cases ep/ep-algset)
           even-cases))
    (is (= (:odd-cases ep/ep-algset)
           odd-cases))))
