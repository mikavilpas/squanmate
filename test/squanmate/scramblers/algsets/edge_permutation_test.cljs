(ns squanmate.scramblers.algsets.edge-permutation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.utils.alg-verification-utils :as av])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest parse-all-ep-cases-test []
  (is (empty? (av/non-parseable-cases ep/all-cases))))

(deftest execute-all-ep-cases-test []
  (is (empty? (av/non-executable-cases ep/all-cases))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases ep/all-cases))))

(deftest parity-of-cases []
  (let [results (av/parity-and-non-parity-cases ep/all-cases)
        odd-cases (get results :switches-parity)
        even-cases (get results :preserves-parity)]
    (is (= (:even-cases ep/ep-algset)
           even-cases))
    (is (= (:odd-cases ep/ep-algset)
           odd-cases))))
