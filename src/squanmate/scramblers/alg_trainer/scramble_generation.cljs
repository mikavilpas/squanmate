(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [squanmate.puzzle :as p]
            [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.solving :as solving]
            [squanmate.rotation :as rotation]))

(defn- apply-random-rotation-of-3 [state puzzle]
  (rotation/possible-rotations ))

(defn new-scramble [state]
  (let [start p/square-square]
    (-> (apply-random-rotation-of-3 state start)
        (solving/solve-and-generate-scramble state))))
