(ns squanmate.scramblers.edge-permutation-scrambler.actions
  (:require [squanmate.puzzle :as p]))

(defn- randomize-edges [puzzle layer-name]
  (let [layer (get puzzle layer-name)
        pieces (:pieces layer)
        corners (filter p/corner? pieces)
        edges (filter p/edge? pieces)

        [new-pieces _] (p/layer-with-pieces {"c" corners
                                             "e" (shuffle edges)}
                                            (p/pieces-str layer))]
    (assoc-in puzzle [layer-name :pieces] new-pieces)))

(defn- scramble [puzzle]
  (-> puzzle
      (randomize-edges :top-layer)
      (randomize-edges :bottom-layer)))

(defn new-scramble []
  (scramble p/square-square))
