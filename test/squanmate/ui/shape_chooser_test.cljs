(ns squanmate.ui.shape-chooser-test
  (:require [squanmate.ui.shape-chooser :as sut]
            [reagent.core :as reagent]
            [clojure.walk :as walk])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defonce test-state (reagent/atom nil))

(defcard-rg chooser
  [sut/chooser
   :options [(sut/make-value :id "value1" :label "Value 1")
             (sut/make-value :id "value2" :label "Value 2")]
   :state test-state])

(defonce test-state2 (reagent/atom nil))
(defcard-rg shape-chooser
  [sut/shape-chooser :state test-state2])
