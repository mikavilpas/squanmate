(ns squanmate.alg.rotation
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as p]
            [squanmate.alg.slicing :as slicing]))

(defprotocol LayerRotationStrategy
  (first-piece [this layer])
  (rotate-one-piece [this layer]))

(def empty-rotation (types/->Rotations 0 0))

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

;; try smaller rotations first to keep possible-rotations simple to understand
(def ^:private all-rotation-amounts [0
                                     1 -1
                                     2 -2
                                     3 -3
                                     4 -4
                                     5 -5
                                     6])

(defn possible-rotations
  ([layer]
   (possible-rotations layer all-rotation-amounts))
  ([layer rotation-amounts]
   (let [rotation-results (map (fn [amount]
                                 [(rotate-layer layer amount)
                                  amount])
                               rotation-amounts)
         successful-results (->> rotation-results
                                 (filter (fn [[result-either _amount]]
                                           (either/right? result-either)))
                                 (map (fn [[result-either amount]]
                                        [(m/extract result-either) amount])))]
     successful-results)))

(defn random-layer-rotations [layer]
  (shuffle (possible-rotations layer)))

(defn sliceable-rotations [layer]
  (let [possible-rotations (possible-rotations layer)]
    (->> possible-rotations
         (filter (fn [[result amount]]
                   (slicing/layer-sliceable? result))))))

(defn random-sliceable-rotations [layer]
  (shuffle (sliceable-rotations layer)))
