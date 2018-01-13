(ns squanmate.alg.rotation-test
  (:require [clojure.test :as t :refer [is]]
            [cats.monad.either :as either]
            [squanmate.puzzle :as p]
            [squanmate.alg.rotation :as r]
            [squanmate.services.shapes :as shapes]
            [cats.core :as m]
            [squanmate.puzzle :as puzzle])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def square-top-layer (:top-layer puzzle/square-square))
(def square-bottom-layer (:bottom-layer puzzle/square-square))

(defn- rotated-pieces [layer amount]
  (m/fmap p/pieces-str
          (r/rotate-layer layer amount)))

(deftest first-piece-top-layer-test []
  "top layer"
  (is (= "e"
         (->> (r/first-piece r/clockwise-rotation square-top-layer)
              :type)))
  (is (= "c"
         (->> (r/first-piece r/counterclockwise-rotation square-top-layer)
              :type)))

  "bottom layer"
  (is (= "c"
         (->> (r/first-piece r/clockwise-rotation square-bottom-layer)
              :type)))
  (is (= "e"
         (->> (r/first-piece r/counterclockwise-rotation square-bottom-layer)
              :type))))

(deftest rotate-layer-clockwise-test []
  "dummy rotation must do nothing"
  (is (= (r/rotate-layer square-top-layer 0)
         (either/right square-top-layer)))

  (is (= (rotated-pieces square-top-layer 1)
         (either/right "ecececec")))

  (is (= (rotated-pieces square-top-layer 3)
         (either/right "cececece"))
      "rotating in 90 degree increments must preserve the square shape")

  "rotating by other amounts"
  (is (= (rotated-pieces square-top-layer 4)
         (either/right "ecececec")))

  (is (either/left?
       (r/rotate-layer shapes/eight -1))
      "it should be impossible to rotate by -1 if the next piece is worth 2"))

(deftest rotate-layer-counterclockwise-test []
  (is (= (rotated-pieces square-top-layer -2)
         (either/right "ecececec"))
      "rotate -2 over a corner piece")

  (is (either/left? (r/rotate-layer square-top-layer -1))
      "trying to rotate -1 when the first piece is a corner piece (worth 2)"))

(defn rotation-strings [layer]
  (->> layer
       r/possible-rotations
       (map (fn [[p amount]]
              [(puzzle/pieces-str p)
               amount]))))

(deftest possible-rotations-test []
  (is (= [["cceeecce" 0]
          ["ecceeecc" 1]
          ["ceeeccec" -2]
          ["cecceeec" 3]
          ["eeeccecc" -4]
          ["ccecceee" 5]
          ["eeccecce" -5]
          ["ecceccee" 6]]
         (rotation-strings shapes/mushroom))))
