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
