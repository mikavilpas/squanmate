(ns squanmate.puzzle
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

;; the order of the pieces starts at the front equator, and goes clockwise
(defrecord Layer [pieces])
(defrecord Piece [type])
(defrecord LayerError [msg layer])

(def edge (Piece. "e"))
(def corner (Piece. "c"))

(def square-layer
  (let [e edge
        c corner]
    (Layer. [c e c e
             c e c e])))

(defn piece-value [piece]
  (condp = (:type piece)
    "e" 2
    "c" 1
    (throw (str "unknown piece " (pr-str piece)))))

(defn layer-sliceable? [layer]
  ;; Either the top or bottom layer can be sliced if it has an edge of a piece
  ;; at the slice point. The slice always happens at a distance of 6.
  (let [values (map piece-value (:pieces layer))
        sums (reductions + values)]
    (some (partial = 6) sums)))

(defn- rotate-layer-clockwise [layer amount]
  (if (zero? amount)
    (either/right layer)
    (let [pieces (:pieces layer)
          this-piece (first pieces)
          piece-amount (piece-value this-piece)
          new-amount (- amount piece-amount)]
      (if (< new-amount 0)
        (either/left (LayerError. (str "cannot turn "
                                       amount
                                       " when the next piece is worth "
                                       piece-amount)
                                  layer))
        (let [new-layer (assoc-in layer [:pieces]
                                  (conj (vec (next pieces))
                                        this-piece))]
          (recur new-layer new-amount))))))

(defn- rotate-layer-counterclockwise [layer amount]
  (either/right layer))

(defn rotate-layer [layer amount]
  (cond
    (= 0 amount) (either/right layer)
    (pos-int? amount) (rotate-layer-clockwise layer amount)
    (neg-int? amount) (rotate-layer-counterclockwise layer amount)
    :else (either/left (LayerError. "rotate-layer: unknown case"
                                    layer))))
