(ns squanmate.services.count-positions
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.rotation :as rotation]
            [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.ui.initial-rotation-adjuster :as initial-rotation-adjuster]
            [squanmate.ui.rotation-adjuster-controls :as rac]
            [squanmate.alg.prettification :as prettification]))

(defn- valid-bottom-layer-for-top-layer [top-layer]
  ;; choose the same one each time (first of the sorted layer names) so that
  ;; the user isn't surprised by the count positions switching unexpectedly
  (let [layer-name (shapes/layer-shape-name-key top-layer)
        bottom-layer-name (-> (shape-combinations/filtered-possible-shapes layer-name)
                               list*
                               sort
                               first)]
    (get shapes/all-shapes bottom-layer-name)))

(defn- puzzle-with-top-layer
  "Construct a Puzzle using the given top-layer. Choose any valid bottom layer."
  [top-layer]
  (let [layer-pieces (p/pieces-str top-layer)
        foo (-> top-layer
                valid-bottom-layer-for-top-layer)
        bottom-layer-pieces (-> top-layer
                                valid-bottom-layer-for-top-layer
                                p/pieces-str)]
    (p/puzzle-with-shapes layer-pieces
                          bottom-layer-pieces)))

(defn- parity?-and-rotation-amount [puzzle [rotated-layer amount]]
  (let [new-puzzle (assoc puzzle
                          :top-layer rotated-layer)
        [odd-parity? _] (parity-counter/parity-count new-puzzle)
        adjusted-value (-> (+ amount 6)
                           prettification/prettify-value)]
    [odd-parity? adjusted-value]))

(defn- filter-parities [filter-fn parities-and-rotations]
  (into #{} (for [[parity? amount] parities-and-rotations
                  :when (filter-fn parity?)]
              amount)))

(defn count-positions
  "Results are given in two sets of rotations relative to the current layer
  position.

  Only sliceable positions are supported!

  NB: It is not possible to know whether a count position alone has odd or even
  parity. the entire puzzle is required for a parity calculation instead. Even
  when the entire puzzle is used, the actual parity count of the different
  positions will change.

  The important and unchanging thing about the positions are thus the positions
  themselves: each group of positions will always report the same (even or odd)
  parity."
  [layer]
  (let [puzzle (puzzle-with-top-layer layer)
        rotation-results-and-amounts (rotation/sliceable-rotations
                                      (:top-layer puzzle))
        parities-and-rotations (map #(parity?-and-rotation-amount puzzle %)
                                    rotation-results-and-amounts)]
    #{(filter-parities true? parities-and-rotations)
      (filter-parities false? parities-and-rotations)}))
