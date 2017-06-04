(ns squanmate.slicing-test
  (:require [squanmate.slicing :as slicing]
            [clojure.test :as t :refer [is run-tests]]
            [squanmate.puzzle :as p]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [cats.monad.either :as either]
            [cats.core :as m])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]))

(defcard-rg square-square-to-kite-kite
  [:div
   (monochrome/monochrome-puzzle p/square-square)
   (m/mlet [result (slicing/slice p/square-square)]
           (monochrome/monochrome-puzzle result))])

(deftest slice-square-square-to-kite-kite-test []
  (m/mlet [result (slicing/slice p/square-square)]
          ;; todo is kite kite
          (is (= "ceceecec" (p/pieces-str (:top-layer result))))
          (is (= "ececcece" (p/pieces-str (:bottom-layer result))))))

(def e p/edge)
(def c p/corner)
(def kite-kite (p/Puzzle. (p/TopLayer. [e c c e c e e c])
                          (p/BottomLayer. [e c e c c e c e])))

(defcard-rg slicing-to-opposite-fist-visualization
  (m/mlet [result (slicing/slice kite-kite)]
          [:div "this test slices kite-kite to get opposite fists:"
           (monochrome/monochrome-puzzle kite-kite)
           (monochrome/monochrome-puzzle result)]))

(deftest slice-kite-kite-to-opposite-fists-test []
  (m/mlet [result (slicing/slice kite-kite)]
          (is (= "eccecece" (p/pieces-str (:top-layer result))))
          (is (= "ececceec" (p/pieces-str (:bottom-layer result))))))

(deftest layer-sliceable?-test []
  (is (true? (slicing/layer-sliceable? (:top-layer p/square-square)))))
