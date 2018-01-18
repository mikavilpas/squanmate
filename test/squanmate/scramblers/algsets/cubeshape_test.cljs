(ns squanmate.scramblers.algsets.cubeshape-test
  (:require [squanmate.scramblers.algsets.cubeshape :as cubeshape]
            [clojure.test :as t :refer [is]]
            [squanmate.utils.alg-verification-utils :as av]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(def all-cases (algset-scrambler/all-cases cubeshape/cubeshape-algset))

(deftest parity-of-cases []
  (let [results (av/parity-and-non-parity-puzzles cubeshape/cubeshape-algset all-cases)]
    (is (= {:even-parity-at-cubeshape (:even-cases cubeshape/cubeshape-algset)
            :odd-parity-at-cubeshape (:odd-cases cubeshape/cubeshape-algset)}
           results))))
