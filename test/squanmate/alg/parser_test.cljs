(ns squanmate.alg.parser-test
  (:require [squanmate.alg.parser :as a]
            [clojure.test :as t :refer [is]]
            [the.parsatron :as p]
            [squanmate.alg.types :as types]
            [cats.monad.either :as either])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]
   [the.parsatron :refer [let->> >> defparser]]))

;; test readabality
(def slice (types/Slice.))

(defn rotate-top [n]
  (types/RotateTopLayer. n))

(defn rotate-bottom [n]
  (types/RotateBottomLayer. n))

(deftest parse-integer-test []
  (is (= 123 (p/run (a/integer) "123")))
  (is (= -123 (p/run (a/integer) "-123"))))

(deftest rotation-instruction-test []
  (is (= [(rotate-top -2)
          (rotate-bottom 3)]
         (p/run (a/rotation-instruction) "-2, 3"))))

(deftest rotation-instruction-top-layer-only-test []
  (is (= [(rotate-top -3)]
         (p/run (a/rotation-instruction-top-layer-only)
           "-3"))))

(deftest algorithm-test []
  (is (= (either/right [(rotate-top 1)
                        (rotate-bottom 2)
                        slice])
         (a/parse "1,2/")))

  "leading slice"
  (is (= (either/right [slice
                        (rotate-top 1)
                        (rotate-bottom 2)
                        slice])
         (a/parse "/1,2/")))

  "an alg that leaves out the bottom layer 0 value"
  (is (= (either/right [slice
                        (rotate-top 3)
                        slice])
         (a/parse "/3/")))

  "an alg that has rotation steps in (parentheses)"
  (is (= (either/right [slice
                        (rotate-top 3)
                        (rotate-bottom 3)
                        slice])
         (a/parse "/ (3, 3) /")))

  "adjacent parity algorithm on many lines, including a * at the end"
  (is (= (either/right [slice (rotate-top -3)
                        slice (rotate-bottom 3)
                        slice (rotate-bottom -3)
                        slice (rotate-bottom 3)
                        slice (rotate-top 2)
                        slice (rotate-bottom 2)
                        slice (rotate-top -2)
                        slice (rotate-top 4)
                        slice (rotate-bottom -2)
                        slice (rotate-bottom 2)
                        slice (rotate-top -1) (rotate-bottom 4)
                        slice (rotate-bottom -3)
                        slice])
         (a/parse "/ -3,0/0,3/0,-3/0,3/
                    2,0/0,2/-2,0/4,0
                   /0,-2/ 0,2/-1,4/0,-3/*"))))
