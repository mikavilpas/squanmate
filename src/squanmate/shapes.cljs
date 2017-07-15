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

(def square (Shape. "Square" [c e c e c e c e]))
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
(def parallel-edges (Shape. "Parallel edges" [c e c c c e c]))

(def star (Shape. "Star" [c c c c c c]))

(def all-shapes {"four-four" four-four
                 "five-three" five-three
                 "six-two" six-two
                 "seven-one" seven-one
                 "eight" eight
                 "two-two-two" two-two-two
                 "three-three" three-three
                 "three-two-one" three-two-one
                 "three-one-two" three-one-two
                 "left-four-two" left-four-two
                 "right-four-two" right-four-two
                 "four-one-one" four-one-one
                 "left-five-one" left-five-one
                 "right-five-one" right-five-one
                 "six" six
                 "square" square
                 "kite" kite
                 "barrel" barrel
                 "shield" shield
                 "left-fist" left-fist
                 "right-fist" right-fist
                 "left-pawn" left-pawn
                 "right-pawn" right-pawn
                 "mushroom" mushroom
                 "scallop" scallop
                 "paired-edges" paired-edges
                 "perpendicular-edges" perpendicular-edges
                 "parallel-edges" parallel-edges
                 "star" star})

(defn ordered-permutations
  "Given a layer's pieces, returns a lazy seq of all the possible rotated states
  the layer can have.

  Actually works with any elements, not just pieces"
  [elements]
  (lazy-seq
   (for [i (range (count elements))]
     (let [[front back] (split-at i elements)]
       ;; inverse order
       (vec (concat back front))))))

(defn- same-piece-type-order? [pieces-a pieces-b]
  (= (map :type pieces-a)
     (map :type pieces-b)))

(defn same-shape? [layer-a layer-b]
  (let [a-perms (ordered-permutations (:pieces layer-a))]
    (->> a-perms
         (some (partial same-piece-type-order?
                  (:pieces layer-b))))))

(defn layer-shape-name [layer]
  (->> (vals all-shapes)
       (filter (partial same-shape? layer))
       first
       :name))
