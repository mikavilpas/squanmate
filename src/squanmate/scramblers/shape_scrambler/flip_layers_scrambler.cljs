(ns squanmate.scramblers.shape-scrambler.flip-layers-scrambler
  (:require [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.scramblers.shape-scrambler.default-scrambler :as default-scrambler]
            [squanmate.services.shapes :as shapes]
            [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as pps]))

(defn- flip-layers [puzzle]
  (assoc puzzle
         :top-layer (:bottom-layer puzzle)
         :bottom-layer (:top-layer puzzle)))

(defrecord FlipLayersScrambler [ref-puzzle]
  scrambler/ShapeScrambler
  (create-scramble [self]
    (let [puzzle (-> ref-puzzle
                     flip-layers
                     pps/puzzle-with-same-layers
                     default-scrambler/apply-random-rotations)
          puzzle-layer-names (shapes/puzzle-layer-shape-names puzzle)]
      [puzzle-layer-names puzzle])))
