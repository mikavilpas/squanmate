(ns squanmate.ui.parity-analysis-test
  (:require [squanmate.ui.parity :as sut]
            [clojure.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg oi
  [:div "hello"])
