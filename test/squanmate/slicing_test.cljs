(ns squanmate.slicing-test
  (:require [squanmate.slicing :as slicing]
            [clojure.test :as t :refer [is run-tests]]
            [squanmate.puzzle :as p]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.alg.rotation :as r]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]))

(defcard-rg square-square-to-kite-kite
  [:div
   (newmonochrome/monochrome-puzzle p/square-square)
   (m/mlet [result (slicing/slice p/square-square)]
           (newmonochrome/monochrome-puzzle result))])

(deftest slice-square-square-to-kite-kite-test []
  (m/mlet [result (slicing/slice p/square-square)]
          ;; todo is kite kite
          (is (= "ceceecec" (p/pieces-str (:top-layer result))))
          (is (= "ceceecec" (p/pieces-str (:bottom-layer result))))))

(def kite-kite (shapes/puzzle-with-layers "kite" "kite"))

(deftest layer-sliceable?-test []
  (is (true? (slicing/layer-sliceable? (:top-layer p/square-square))))

  "This should go to left-fist right-fist but be unable to slice the top layer"
  (m/mlet [position (execution/transformation-result kite-kite "/-1")]
          (is (not (slicing/layer-sliceable? (:top-layer position))))))

(deftest pieces-and-their-positions-test []
  (is (= [["c" 0]
          ["e" 2]
          ["c" 3]
          ["e" 5]
          ["c" 6]
          ["e" 8]
          ["c" 9]
          ["e" 11]]
         (map (fn [[p position]]
                [(:type p) position])
              (slicing/pieces-and-their-positions shapes/square)))))
