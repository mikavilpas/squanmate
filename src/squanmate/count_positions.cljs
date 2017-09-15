(ns squanmate.count-positions
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.rotation :as rotation]
            [squanmate.alg.parity-counter :as parity-counter]))

(def ^:private kite-pieces "ceceecec")

(defn- assoc-dummy-kite-layer [layer]
  (let [layer-pieces (p/pieces-str layer)]
    (p/puzzle-with-shapes layer-pieces
                          kite-pieces)))

(defn- parity?-and-rotation-amount [puzzle [rotated-layer amount]]
  (let [new-puzzle (assoc puzzle
                          :top-layer rotated-layer)
        [odd-parity? _] (parity-counter/parity-count new-puzzle)]
    [odd-parity? amount]))

(defn- filter-parities [filter-fn parities-and-rotations]
  (into #{} (for [[parity? amount] parities-and-rotations
                  :when (filter-fn parity?)]
              amount)))

(defn count-positions
  "Results are given in two sets of rotations relative to the current layer
  position.

  NB: It is not possible to know whether a count position alone has odd or even
  parity. the entire puzzle is required for a parity calculation instead."
  [layer]
  (let [puzzle (assoc-dummy-kite-layer layer)
        rotation-results-and-amounts (rotation/possible-rotations
                                      (:top-layer puzzle))
        parities-and-rotations (map #(parity?-and-rotation-amount puzzle %)
                                    rotation-results-and-amounts)]
    #{(filter-parities true? parities-and-rotations)
      (filter-parities false? parities-and-rotations)}))
