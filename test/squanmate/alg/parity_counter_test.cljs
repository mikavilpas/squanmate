(ns squanmate.alg.parity-counter-test
  (:require [squanmate.alg.parity-counter :as c]
            [clojure.test :as t :refer [is]]
            [squanmate.alg.puzzle :as p]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cljs.pprint :as pprint]
            [squanmate.services.shapes :as shapes]
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
   [newmonochrome/monochrome-puzzle cube]

   "pieces in count order:"
   [:pre (pprint (c/pieces-in-count-order cube))]])

(deftest parity-count-solved-test []
  "The parity count at cubeshape is odd (to get an even parity count, one must
  misalign the top or bottom layer). This tests that an odd result is counted
  for a solved puzzle."
  (let [[has-parity? pc] (c/parity-count cube)]
    (is (true? has-parity?))
    (is (zero? (-> pc :top-corner-order :parity-count)))
    (is (= 1 (-> pc :top-edge-order :parity-count)))
    (is (= 1 (-> pc :bottom-corner-order :parity-count)))
    (is (= 1 (-> pc :bottom-edge-order :parity-count)))
    (is (= 2 (-> pc :top-edges-in-odd-edge-positions :parity-count)))
    (is (= 2 (-> pc :top-corners-in-odd-corner-positions :parity-count)))))

(defcard-rg pieces-in-count-order-kite-kite
  [:div
   "Here we inspect the kite kite shape, in order to debug the parity count. The
   result can be tested in a unit test below separately."
   [newmonochrome/monochrome-puzzle kite-kite]

   "pieces in count order:"
   [:pre (pprint (c/pieces-in-count-order kite-kite))]])
