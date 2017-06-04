(ns squanmate.puzzle
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

(defrecord Puzzle [top-layer bottom-layer])

;; the order of the pieces starts at the bottom left corner (sort of), and goes clockwise
(defrecord TopLayer [pieces])
(defrecord BottomLayer [pieces])

(defrecord Piece [type])
(defrecord LayerError [msg layer])

(def edge (Piece. "e"))
(def corner (Piece. "c"))

;; todo make a module that knows different shapes
(def square-square
  (let [e edge
        c corner]
    (Puzzle.
     (TopLayer. [c e c e
                 c e c e])
     (BottomLayer. [c e c e
                    c e c e]))))

(defn piece-value [piece]
  (condp = (:type piece)
    "e" 2
    "c" 1
    (throw (str "unknown piece " (pr-str piece)))))
