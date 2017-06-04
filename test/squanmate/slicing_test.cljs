(ns squanmate.slicing-test
  (:require [squanmate.slicing :as slicing]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [cats.monad.either :as either]
            [cats.core :as m])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]))

(defcard-rg square-square
  [:div
   "slicing square square..."
   (monochrome/monochrome-puzzle p/square-square)
   "result:"
   (m/mlet [result (slicing/slice p/square-square)]
           (monochrome/monochrome-puzzle result))])

(deftest slice-test []
  (let [result (slicing/slice p/square-square)]
    (is (either/right? result))
    (is (= "TODO kite kite" result))))

(deftest layer-sliceable?-test []
  (is (true? (slicing/layer-sliceable? (:top-layer p/square-square)))))
