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

(defn- rotate [top bottom]
  (types/Rotations. top (or bottom 0)))

(deftest parse-integer-test []
  (is (= 123 (p/run (a/integer) "123")))
  (is (= -123 (p/run (a/integer) "-123"))))

(deftest rotation-instruction-test []
  (is (= [(rotate -2 3)]
         (p/run (a/rotation-instruction) "-2, 3"))))

(deftest rotation-instruction-top-layer-only-test []
  (is (= [(rotate -3 0)]
         (p/run (a/rotation-instruction-top-layer-only)
           "-3"))))

(deftest algorithm-test []
  (is (= (either/right [(rotate 1 2)
                        slice])
         (a/parse "1,2/")))

  "leading slice"
  (is (= (either/right [slice
                        (rotate 1 2)
                        slice])
         (a/parse "/1,2/")))

  "rotation only (no slice)"
  (is (= (either/right [(rotate 3 -2)])
         (a/parse "3, -2")))

  "an alg that leaves out the bottom layer 0 value"
  (is (= (either/right [slice
                        (rotate 3 0)
                        slice])
         (a/parse "/3/")))

  "an alg that has rotation steps in (parentheses)"
  (is (= (either/right [slice
                        (rotate 3 3)
                        slice])
         (a/parse "/ (3, 3) /")))

  "adjacent parity algorithm on many lines, including a * at the end"
  (is (= (either/right [slice (rotate -3 0)
                        slice (rotate 0 3)
                        slice (rotate 0 -3)
                        slice (rotate 0 3)
                        slice (rotate 2 0)
                        slice (rotate 0 2)
                        slice (rotate -2 0)
                        slice (rotate 4 0)
                        slice (rotate 0 -2)
                        slice (rotate 0 2)
                        slice (rotate -1 4)
                        slice (rotate 0 -3)
                        slice])
         (a/parse "/ -3,0/0,3/0,-3/0,3/
                    2,0/0,2/-2,0/4,0
                   /0,-2/ 0,2/-1,4/0,-3/*"))))
