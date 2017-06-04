(ns squanmate.alg-executor-test
  (:require [squanmate.alg-executor :as a]
            [clojure.test :as t :refer [is]]
            [the.parsatron :as p])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]
   [the.parsatron :refer [let->> >> defparser]]))

(deftest parse-integer-test []
  (is (= 123 (p/run (a/integer) "123")))
  (is (= -123 (p/run (a/integer) "-123"))))

(deftest rotation-instruction-test []
  (is (= [(a/RotateTopLayer. -2)
          (a/RotateBottomLayer. 3)] (p/run (a/rotation-instruction) "-2, 3"))))
