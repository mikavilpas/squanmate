(ns squanmate.rotation-test
  (:require [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as r])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def e p/edge)
(def c p/corner)

(def square-layer (:top-layer p/square-square))

(deftest rotate-layer-clockwise-test []
  "dummy rotation must do nothing"
  (is (= (r/rotate-layer square-layer 0)
         (either/right square-layer)))

  (let [expected-square (either/right square-layer)]
    (is (= (r/rotate-layer square-layer 3) expected-square)
        "rotating in 90 degree increments must preserve the square shape"))

  "rotating by other amounts"
  (is (= (r/rotate-layer square-layer 1)
         (either/right (p/TopLayer. [e c e c
                                     e c e c]))))
  (is (= (r/rotate-layer square-layer 4)
         (either/right (p/TopLayer. [e c e c
                                     e c e c])))))

(deftest rotate-layer-counterclockwise-test []
  (is (= (r/rotate-layer square-layer -2)
         (either/right (p/TopLayer. [e c e c
                                     e c e c])))
      "rotate -2 over a corner piece")

  (let [result (r/rotate-layer square-layer -1)]
    (is (either/left? result)
        "trying to rotate -1 when the first piece is a corner piece (worth 2)")))
