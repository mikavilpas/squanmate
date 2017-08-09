(ns squanmate.rotation
  (:require [cats.monad.either :as either]
            [squanmate.puzzle :as p]
            [cats.core :as m]))

(defprotocol LayerRotationStrategy
  (first-piece [this layer])
  (rotate-one-piece [this layer]))

(defn- rotate-layer-worker [rotation-strategy layer amount]
  (if (zero? amount)
    (either/right layer)
    (let [first-piece (first-piece rotation-strategy layer)
          piece-amount (p/piece-value first-piece)
          new-amount (- amount piece-amount)]
      (if (< new-amount 0)
        (either/left (p/LayerError. (str "cannot turn "
                                         amount
                                         " when the next piece is worth "
                                         piece-amount)
                                    layer))
        (let [new-layer (rotate-one-piece rotation-strategy layer)]
          (recur rotation-strategy new-layer new-amount))))))

(def clockwise-rotation
  (reify LayerRotationStrategy
    (first-piece [this layer]
      (last (:pieces layer)))
    (rotate-one-piece [this layer]
      (let [pieces (:pieces layer)]
        (assoc-in layer [:pieces]
                  (apply vector
                         (last pieces)
                         (butlast pieces)))))))

(def counterclockwise-rotation
  (reify LayerRotationStrategy
    (first-piece [this layer]
      (first (:pieces layer)))
    (rotate-one-piece [this layer]
      (let [pieces (:pieces layer)]
        (assoc-in layer [:pieces]
                  (into (vec (next pieces))
                        (vector (first-piece this layer))))))))

(defn rotate-layer [layer amount]
  (cond
    (= 0 amount) (either/right layer)
    (pos-int? amount) (rotate-layer-worker clockwise-rotation layer amount)
    (neg-int? amount) (rotate-layer-worker counterclockwise-rotation layer (- amount))
    :else (either/left (p/LayerError. (str "rotate-layer: unknown case when rotating by "
                                           amount)
                                      layer))))

(defn random-layer-rotations [layer]
  (let [rotation-amounts (shuffle (range -6 7))
        rotation-results (map (fn [amount]
                                [(rotate-layer layer amount)
                                 amount])
                              rotation-amounts)
        successful-results (filter (fn [[result _amount]]
                                     (either/right? result))
                                   rotation-results)]
    successful-results))
