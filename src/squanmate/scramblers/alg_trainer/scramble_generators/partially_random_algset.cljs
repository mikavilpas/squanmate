(ns squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset
  "Creates puzzle instances based on an algorithm and some pieces whose
  positions can be randomized. Only works for algs that start at square square."
  (:require [squanmate.scramblers.alg-trainer.scramble-generators.case-by-case-algset :as
             case-by-case-algset]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.puzzle :as p]
            [squanmate.services.cubeshape-piece-swapper :as cubeshape-piece-swapper]
            [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as pps]
            [cats.monad.either :as either]))

(defn randomize-piece-group [puzzle pieces-to-randomize]
  (let [substitutions (zipmap pieces-to-randomize
                              (shuffle pieces-to-randomize))
        new-puzzle (cubeshape-piece-swapper/swap-pieces puzzle substitutions)]
    new-puzzle))

(defn randomize-piece-groups [puzzle piece-groups-to-randomize]
  (reduce randomize-piece-group
          puzzle
          piece-groups-to-randomize))

(defn- solve-to-starting-position [alg start-puzzle]
  (let [alg (case-by-case-algset/prepend-random-rotations alg)]
    (-> (execution/transformation-result-reverse start-puzzle alg)
        execution/puzzle-of-result)))

(defn create-puzzle
  "`piece-groups-to-randomize` should be a sequence of sequences of pieces, like

  [[:ul-edge :ur-edge :ub-edge :uf-edge], [:ulb-corner :ubr-corner]]

  All of the pieces in each group will be exchanged randomly.
  "
  [alg-string piece-groups-to-randomize]
  (let [puzzle (case-by-case-algset/starting-puzzle)]
    (-> alg-string
        (solve-to-starting-position puzzle)
        (randomize-piece-groups piece-groups-to-randomize))))

(defn create-case-with-parity [alg edges-to-randomize want-odd-parity?]
  ;; At square square, the default positions (aligned) will report an odd parity
  ;; count. Thus want-odd-parity? will work like intended.
  (let [new-puzzles (repeatedly #(create-puzzle alg edges-to-randomize))]
    (first (filter (partial pps/same-parity? (either/right want-odd-parity?))
                   new-puzzles))))
