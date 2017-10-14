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

(def ^:private M2-steps [(rotate 1 0)
                         slice
                         (rotate -1 -1)
                         slice
                         (rotate 0 1)])

(deftest parse-integer-test []
  (is (= 123 (p/run (a/integer) "123")))
  (is (= -123 (p/run (a/integer) "-123"))))

(deftest rotation-instruction-test []
  (is (= (rotate -2 3)
         (p/run (a/rotation-instruction) "-2, 3"))))

(deftest rotation-instruction-top-layer-only-test []
  (is (= (rotate -3 0)
         (p/run (a/rotation-instruction-top-layer-only)
           "-3"))))

(deftest algorithm-test []
  (is (= (either/right [])
         (a/parse ""))
      "the empty alg")

  (is (= (either/right [])
         (a/parse " "))
      "the whitespace alg")

  (is (= (either/right [slice])
         (a/parse "/"))
      "only a slice")

  (is (= (either/right [(rotate 1 2)
                        slice])
         (a/parse "1,2/")))

  "leading slice"
  (is (= (either/right [slice
                        (rotate 1 2)
                        slice])
         (a/parse "/1,2/")))

  (is (= (either/right [(rotate -3 0)
                        slice])
         (a/parse "(-3,0)/"))
      "trailing slice")

  (is (= (either/right [(rotate -3 0)
                        slice])
         (a/parse "(-3,0)/       "))
      "trailing whitespace")

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
                   /0,-2/ 0,2/-1,4/0,-3/*")))

  (is (either/right?
       (a/parse " /( 0, 3)/(-4, 1)/(-4, 4)/(-2, 2)/(-4, 1)/( 3, 0)/( 0, 2)/( 4, 1)/( 2, 5)/(-2, 1)/(-1, 2)/(-2, 1)/( 0, 3)/( 5, 5)/( 6,-2)"))
      "An alg given as a response by jaap's solver")

  (is (either/right?
       (a/parse " ( 1, 0)/( 3, 0)/(-1, 2)/( 6, 3)/( 1, 1)/( 5, 2)/(-5, 4)/( 2,-1)/( 0, 1)"))
      "Another alg given by jaap's solver"))

(deftest parse-face-moves-test []
  (is (= (either/right [(rotate 3 0)])
         (a/parse "U")))
  (is (= (either/right [(rotate -3 0)])
         (a/parse "U'")))

  (is (= (either/right [(rotate 0 3)])
         (a/parse "D")))
  (is (= (either/right [(rotate 0 -3)])
         (a/parse "D'")))

  (is (= (either/right [(rotate 6 0)])
         (a/parse "U2")))
  (is (= (either/right [(rotate 0 6)])
         (a/parse "D2")))

  (is (= (either/right [(rotate -3 0)])
         (a/parse "U’"))
      "special curved single quote (’)")
  (is (= (either/right [(rotate 0 -3)])
         (a/parse "D’"))
      "special curved single quote (’)"))

(deftest parse-m2-test []
  (is (= (either/right (concat M2-steps
                               M2-steps))
         (a/parse "M2 M2")))

  (is (= (either/right (concat M2-steps
                               [(rotate -3 0)]
                               M2-steps
                               [(rotate 3 0)]
                               M2-steps))
         (a/parse "M2 U’ M2 U M2"))))

(deftest parser-fails-test []
  (is (either/left? (a/parse "not an algorithm"))))
