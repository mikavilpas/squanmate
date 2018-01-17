(ns squanmate.scramblers.algsets.lin-corner-permutation-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.scramblers.algsets.lin-corner-permutation :as lin-cp]
            [squanmate.utils.alg-verification-utils :as av]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def all-cases (algset-scrambler/all-cases lin-cp/lin-cp-algset))

(deftest parse-all-lin-cp-cases-test []
  (is (empty? (av/non-parseable-cases all-cases))))

(deftest execute-all-lin-cp-cases-test []
  (is (empty? (av/non-executable-cases all-cases))))

(deftest all-cases-are-aligned-test []
  (is (empty? (av/non-aligned-cases all-cases))))

(deftest parity-of-cases []
  (let [results (av/parity-and-non-parity-puzzles lin-cp/lin-cp-algset all-cases)]
    (is (= {:even-parity-at-cubeshape (:even-cases lin-cp/lin-cp-algset)
            :odd-parity-at-cubeshape (:odd-cases lin-cp/lin-cp-algset)}
           results))))
