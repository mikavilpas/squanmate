(ns squanmate.ui.count-position-finder-test
  (:require [squanmate.ui.count-position-finder :as sut]
            [cljs.test :as t :include-macros true])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce state (sut/default-state))

(defcard-rg count-position-finder
  [sut/count-position-finder state])
