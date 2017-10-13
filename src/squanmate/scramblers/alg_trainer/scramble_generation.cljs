(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [squanmate.puzzle :as p]
            [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.solving :as solving]
            [squanmate.rotation :as rotation]))

(defn- apply-random-rotation-of-3 [puzzle]
  (letfn [(rotate [layer]
            (let [[new-layer _amount] (rand-nth (rotation/possible-rotations
                                                 layer
                                                 [0 3 -3 6]))]
              new-layer))]
    (update puzzle
            :top-layer rotate
            :bottom-layer rotate)))

(defn new-scramble [state]
  (let [start (apply-random-rotation-of-3 p/square-square)]
    (solving/solve-and-generate-scramble start state)))
