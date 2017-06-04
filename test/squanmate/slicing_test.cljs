(ns squanmate.slicing-test
  (:require [squanmate.slicing :as slicing]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [cats.monad.either :as either]
            [cats.core :as m])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]))

(def square-square (p/Puzzle. p/square-layer p/square-layer))

(defcard-rg square-square
  [:div
   "slicing square square..."
   (monochrome/monochrome-puzzle square-square)
   "result:"
   (m/mlet [result (slicing/slice square-square)]
           (monochrome/monochrome-puzzle result))])

(deftest slice-test []
  (let [result (slicing/slice square-square)]
    (is (either/right? result))
    (is (= "TODO kite kite" result))))

(deftest layer-sliceable?-test []
  (is (true? (slicing/layer-sliceable? p/square-layer))))
