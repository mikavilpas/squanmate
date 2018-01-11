(ns squanmate.services.alg-insights-test
  (:require [cats.core :as m]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.services.alg-insights :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))
