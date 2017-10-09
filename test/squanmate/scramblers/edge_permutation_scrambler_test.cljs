(ns squanmate.scramblers.edge-permutation-scrambler-test
  (:require [squanmate.scramblers.edge-permutation-scrambler :as sut]
            [clojure.test :as t :refer [is]]
            [cljs.test :as t :include-macros true])
  (:require-macros [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg lul
  [:div "hello"])
