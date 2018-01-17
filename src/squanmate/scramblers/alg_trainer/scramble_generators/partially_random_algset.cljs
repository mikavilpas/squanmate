(ns squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset
  (:require [squanmate.scramblers.algsets.scramble-generators.case-by-case-algset :as
             case-by-case-algset]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.puzzle :as p]
            [squanmate.services.cubeshape-piece-swapper :as cubeshape-piece-swapper]))

(defn randomize-piece-group [puzzle pieces-to-randomize]
  (let [substitutions (zipmap pieces-to-randomize
                              (shuffle pieces-to-randomize))
        new-puzzle (cubeshape-piece-swapper/swap-pieces puzzle substitutions)]
    new-puzzle))

(defn randomize-piece-groups [puzzle piece-groups-to-randomize]
  (reduce randomize-piece-group
          puzzle
          piece-groups-to-randomize))

(defn- starting-position [alg]
  (let [start-puzzle (case-by-case-algset/starting-puzzle)]
    (-> alg
        (execution/transformation-result-reverse alg)
        execution/puzzle-of-result)))

(defn create-puzzle
  "`piece-groups-to-randomize` should be a sequence of sequences of pieces, like

  [[:ul-edge :ur-edge :ub-edge :uf-edge], [:ulb-corner :ubr-corner]]

  All of the pieces in each group will be exchanged randomly.
  "
  [alg-string piece-groups-to-randomize]
  (-> alg-string
      starting-position
      (randomize-piece-groups piece-groups-to-randomize)))
