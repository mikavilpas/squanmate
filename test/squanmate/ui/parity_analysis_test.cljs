(ns squanmate.ui.parity-analysis-test
  (:require [squanmate.ui.parity-analysis :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg square-square
  [sut/parity-analysis p/square-square])
