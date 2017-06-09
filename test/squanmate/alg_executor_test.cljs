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
          (a/RotateBottomLayer. 3)]
         (p/run (a/rotation-instruction) "-2, 3"))))

(deftest algorithm-test []
  (is (= [(a/RotateTopLayer. 1)
          (a/RotateBottomLayer. 2)
          (a/Slice.)]
         (a/parse "1,2/")))

  "leading slice"
  (is (= [(a/Slice.)
          (a/RotateTopLayer. 1)
          (a/RotateBottomLayer. 2)
          (a/Slice.)]
         (a/parse "/1,2/")))

  "adjacent parity algorithm on many lines, including a * at the end"
  (is (= [(a/Slice.)
          (a/RotateTopLayer. -3)
          (a/RotateBottomLayer. 0)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. 3)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. -3)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. 3)

          (a/Slice.)
          (a/RotateTopLayer. 2)
          (a/RotateBottomLayer. 0)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. 2)

          (a/Slice.)
          (a/RotateTopLayer. -2)
          (a/RotateBottomLayer. 0)

          (a/Slice.)
          (a/RotateTopLayer. 4)
          (a/RotateBottomLayer. 0)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. -2)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. 2)

          (a/Slice.)
          (a/RotateTopLayer. -1)
          (a/RotateBottomLayer. 4)

          (a/Slice.)
          (a/RotateTopLayer. 0)
          (a/RotateBottomLayer. -3)

          (a/Slice.)]
         (a/parse "/ -3,0/0,3/0,-3/0,3/
                  2,0/0,2/-2,0/4,0
                  /0,-2/ 0,2/-1,4/0,-3/*"))))
