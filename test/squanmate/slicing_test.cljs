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

(deftest slice-square-square-to-kite-kite-test []
  (m/mlet [result (slicing/slice p/square-square)]
          ;; todo is kite kite
          (is (= "ceceecec" (p/pieces-str (:top-layer result))))
          (is (= "ececcece" (p/pieces-str (:bottom-layer result))))))

(deftest layer-sliceable?-test []
  (is (true? (slicing/layer-sliceable? (:top-layer p/square-square)))))
