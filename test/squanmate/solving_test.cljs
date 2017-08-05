(ns squanmate.solving-test
  (:require [squanmate.solving :as solving]
            [clojure.test :as t :refer [is async]]
            [cljs.test :as t :include-macros true]
            [cljs.core.async :refer [<!]])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]
   [cljs.core.async.macros :as m :refer [go]]))

(deftest worker-poc-test []
  (is (= 3
         (solving/example-call))))
