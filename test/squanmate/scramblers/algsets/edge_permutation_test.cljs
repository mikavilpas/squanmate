(ns squanmate.scramblers.algsets.edge-permutation-test
  (:require [squanmate.scramblers.algsets.edge-permutation :as ep]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [squanmate.alg.parser :as parser]
            [cats.monad.either :as either]
            [squanmate.ui.common :as common]
            [squanmate.alg.execution :as execution])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defn parse [[case-name alg]]
  [case-name (parser/parse alg)])

(defn non-parseable-cases [cases]
  (let [parse-results (map parse cases)]
    (filter (fn [[case-name result]]
              (either/left? result))
            parse-results)))

(deftest parse-all-ep-cases-test []
  (is (empty? (non-parseable-cases ep/all-cases-unordered))))

(defn execute-reverse [[case-name alg]]
  [case-name
   (execution/transformation-result-reverse p/square-square
                                            alg)])

(defn non-executable-cases [cases]
  (let [execution-results (map execute-reverse cases)]
    (->> execution-results
         (filter (fn [[case-name result]]
                   (either/left? result))))))

(deftest execute-all-ep-cases-test []
  (is (empty? (non-executable-cases ep/all-cases-unordered))))
