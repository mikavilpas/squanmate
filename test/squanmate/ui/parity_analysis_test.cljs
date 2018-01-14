(ns squanmate.ui.parity-analysis-test
  (:require [squanmate.ui.parity-analysis :as sut]
            [clojure.test :as t :refer [is]]
            [squanmate.alg.puzzle :as p]
            [squanmate.services.color-settings :as color-settings])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def colors color-settings/defaults)

(defcard-rg square-square
  [sut/parity-analysis p/square-square colors])
