(ns squanmate.alg.parity-counter-test
  (:require [squanmate.alg.parity-counter :as c]
            [clojure.test :as t :refer [is]]
            [squanmate.puzzle :as p]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cljs.pprint :as pprint]
            [squanmate.shapes :as shapes]
            [squanmate.alg.execution :as execution]
            [cats.core :as m])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def cube p/square-square)
(def kite-kite (-> cube
                   (execution/transformation-result "/0,6")
                   m/extract
                   :puzzle))

(defn- pprint [a] (with-out-str (pprint/pprint a)))

(defcard-rg pieces-in-count-order
  [:div
   [newmonochrome/monochrome-puzzle cube {:monochrome? false}]

   "pieces in count order:"
   [:pre (pprint (c/pieces-in-count-order cube))]])

(deftest parity-count-solved-test []
  "The parity count at cubeshape is odd (to get an even parity count, one must
  misalign the top or bottom layer). This tests that an odd result is counted
  for a solved puzzle."
  (let [[has-parity? pc] (c/parity-count cube)]
    (println pc)
    (is (true? has-parity?))
    (is (zero? (:top-corner-order pc)))
    (is (= 1 (:top-edge-order pc)))
    (is (= 1 (:bottom-corner-order pc)))
    (is (= 1 (:bottom-edge-order pc)))
    (is (= 2 (:top-edges-in-odd-edge-positions pc)))
    (is (= 2 (:top-corners-in-odd-corner-positions pc)))))

(defcard-rg pieces-in-count-order-kite-kite
  [:div
   "Here we inspect the kite kite shape, in order to debug the parity count. The
   result can be tested in a unit test below separately."
   [newmonochrome/monochrome-puzzle kite-kite {:monochrome? false}]

   "pieces in count order:"
   [:pre (pprint (c/pieces-in-count-order kite-kite))]])

(deftest pieces-in-count-order-test []
  (is (= "TODO" (let [foo (c/pieces-in-count-order kite-kite)]
                  foo))))

(deftest parity-count-kite-kite-test []
  (let [[has-parity? pc] (c/parity-count kite-kite)]
    (println pc)
    (is (true? has-parity?))
    (is (zero? (:top-corner-order pc)))
    (is (= 1 (:top-edge-order pc)))
    (is (= 1 (:bottom-corner-order pc)))
    (is (= 1 (:bottom-edge-order pc)))
    (is (= 2 (:top-edges-in-odd-edge-positions pc)))
    (is (= 2 (:top-corners-in-odd-corner-positions pc)))))
