(ns squanmate.puzzle
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

(defrecord Puzzle [top-layer bottom-layer])

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
