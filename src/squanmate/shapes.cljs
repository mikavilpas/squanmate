(ns squanmate.shapes
  (:require [squanmate.puzzle :as puzzle]))

(def e puzzle/edge)
(def c puzzle/corner)

(defrecord Shape [name pieces])

(def four-four (Shape. "4-4" [e c e e e, e c e e e]))
(def five-three (Shape. "5-3" [e c e e e, e e c e e]))
(def six-two (Shape. "6-2" [c e e e e, e e c e e]))
(def seven-one (Shape. "7-1" [c e e e e, e e e c e]))
(def eight (Shape. "8" [c e e e e e, e e e c]))

(def two-two-two (Shape. "2-2-2" [e e c e e, c e e c]))
(def three-three (Shape. "3-3" [e e e c c e e e c]))
(def three-two-one (Shape. "3-2-1" [c e e e c e e c e]))
(def three-one-two (Shape. "3-1-2" [c e e c e e e c e]))
(def left-four-two (Shape. "Left 4-2" [c e e e e c e e c]))
(def right-four-two (Shape. "Right 4-2" [c e e c e e e e c]))
(def four-one-one (Shape. "4-1-1" [e c e e e e c e c]))
(def left-five-one (Shape. "Left 5-1" [c e e e e e c e c]))
(def right-five-one (Shape. "Right 5-1" [c e c e e e e e c]))
(def six (Shape. "6" [c e e e e e e c c]))

(def kite (Shape. "Kite" [c e c e e c e c]))
(def barrel (Shape. "Barrel" [c e e c c e e c]))
(def shield (Shape. "Shield" [e e c c c e e c]))
(def left-fist (Shape. "Left fist" [c e c e c e e c]))
(def right-fist (Shape. "Right fist" [c e e c e c e c]))
(def left-pawn (Shape. "Left pawn" [c c e e e c e c]))
(def right-pawn (Shape. "Right pawn" [c e c e e e c c]))
(def mushroom (Shape. "Mushroom" [c c e e e c c e]))
(def scallop (Shape. "Scallop" [c c e e e e c c]))

(def paired-edges (Shape. "Paired edges" [c c c c c e e]))
(def perpendicular-edges (Shape. "Perpendicular edges" [e c c c c e c]))
(def parallel-edges (Shape. "Parallel edges" [e c c c c e c]))

(def star (Shape. "Star" [c c c c c c]))
