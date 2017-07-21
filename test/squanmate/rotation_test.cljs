(ns squanmate.rotation-test
  (:require [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as r]
            [squanmate.shapes :as shapes]
            [cats.core :as m])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def square-layer shapes/square)

(defn- rotated-pieces [layer amount]
  (m/fmap p/pieces-str
          (r/rotate-layer layer amount)))

(deftest first-piece-test []
  (is (= "e"
         (->> (r/first-piece r/clockwise-rotation square-layer)
              :type))
      "when turning clockwise, must recognize the next piece")

  (is (= "c"
         (->> (r/first-piece r/counterclockwise-rotation square-layer)
              :type))
      "when turning counterclockwise, must recognize the next piece"))

(deftest rotate-layer-clockwise-test []
  "dummy rotation must do nothing"
  (is (= (r/rotate-layer square-layer 0)
         (either/right square-layer)))

  (is (= (rotated-pieces square-layer 1)
         (either/right "ecececec")))

  (is (= (rotated-pieces square-layer 3)
         (either/right "cececece"))
      "rotating in 90 degree increments must preserve the square shape")

  "rotating by other amounts"
  (is (= (rotated-pieces square-layer 4)
         (either/right "ecececec")))

  (is (either/left?
       (r/rotate-layer shapes/eight -1))
      "it should be impossible to rotate by -1 if the next piece is worth 2"))

(deftest rotate-layer-counterclockwise-test []
  (is (= (rotated-pieces square-layer -2)
         (either/right "ecececec"))
      "rotate -2 over a corner piece")

  (is (either/left? (r/rotate-layer square-layer -1))
      "trying to rotate -1 when the first piece is a corner piece (worth 2)"))
