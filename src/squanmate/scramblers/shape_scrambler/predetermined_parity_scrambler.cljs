(ns squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler
  (:require [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.puzzle :as p]
            [squanmate.scramblers.shape-scrambler.default-scrambler
             :as
             default-scrambler]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.alg.serialization :as serialization]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.manipulation :as manipulation]
            [cats.core :as m]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]))

(def ^:private base-scrambler (default-scrambler/new-default-shape-scrambler))

(defn- puzzle-with-same-layers [puzzle]
  (let [top-pieces-str (p/pieces-str (:top-layer puzzle))
        bottom-pieces-str (p/pieces-str (:bottom-layer puzzle))]
    (println "creating a new puzzle")
    (p/puzzle-with-shapes top-pieces-str
                          bottom-pieces-str)))

(defn- reorient-to-default-layer-positions [puzzle]
  (let [{:keys [initial-rotation]} (serialization/puzzle-specification puzzle)
        rotation-to-default-positions (manipulation/reverse-alg initial-rotation)]
    (execution/transformation-result puzzle
                                     rotation-to-default-positions)))

(defn puzzle-parity-at-default-layer-positions [puzzle]
  (let [result-either
        (m/mlet [puzzle-at-default-positions (-> puzzle
                                                 reorient-to-default-layer-positions)]
                (m/return (parity-counter/parity-count (:puzzle puzzle-at-default-positions))))

        ;; consider Left results unrecoverable internal errors
        [parity? _] (m/extract result-either)]
    parity?))

(defn- same-parity? [reference-parity puzzle]
  (= reference-parity
     (puzzle-parity-at-default-layer-positions puzzle)))

(defn create-puzzle-with-relative-parity [ref-puzzle relative-parity-type]
  ;; Note: it's not possible to determine the parity of the puzzle if it is not
  ;; in cubeshape (square square). But it is always possible to find a parity
  ;; count. So one cannot ask for a non-cubeshape puzzle with even/odd parity,
  ;; because that is not possible to create objectively.
  ;;
  ;; Technically it's probably possible to switch the parity of a puzzle by
  ;; swapping two corners or edges to manipulate the parity count (to manipulate
  ;; the parity, that is). But that is a lot more code.. This implementation
  ;; cuts corners by just creating a couple of random puzzles. This is still
  ;; extremely fast for this use case.

  (let [reference-parity (puzzle-parity-at-default-layer-positions ref-puzzle)
        new-puzzles (repeatedly #(puzzle-with-same-layers ref-puzzle))]
    (condp = relative-parity-type
      :same-relative-parity (first (filter (partial same-parity? reference-parity)
                                           new-puzzles)))))

(defn- randomly-switch-top-and-bottom-layers [puzzle]
  ;; at this point the puzzle's layers must be in a sliceable position!
  (if (not (slicing/sliceable? puzzle))
    (throw (new js/Error "Cannot create a new scramble, because the generated
                          puzzle is not at a sliceable position."))

    (if (rand-nth [true false])
      (assoc puzzle
             :top-layer (:bottom-layer puzzle)
             :bottom-layer (:top-layer puzzle)))))

(defrecord PredeterminedParityScrambler [ref-puzzle relative-parity-type]
  scrambler/ShapeScrambler
  (create-scramble [this]
    (let [puzzle (create-puzzle-with-relative-parity ref-puzzle relative-parity-type)]
      (-> puzzle
          randomly-switch-top-and-bottom-layers
          default-scrambler/apply-random-rotations))))
