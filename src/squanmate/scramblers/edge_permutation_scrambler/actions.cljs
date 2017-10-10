(ns squanmate.scramblers.edge-permutation-scrambler.actions
  (:require [squanmate.puzzle :as p]))

(defn- randomize-edges [puzzle layer-name]
  (let [layer (get puzzle layer-name)
        [corners edges] (p/layer-pieces-by-type layer)
        [new-pieces _] (p/layer-with-pieces {"c" corners
                                             "e" (shuffle edges)}
                                            (p/pieces-str layer))]
    (assoc-in puzzle [layer-name :pieces] new-pieces)))

(defn- scramble [puzzle]
  (-> puzzle
      (randomize-edges :top-layer)
      (randomize-edges :bottom-layer)))

(defn new-scramble []
  (let [puzzle (scramble p/square-square)]
    puzzle))
