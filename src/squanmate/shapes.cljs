(ns squanmate.shapes
  (:require [squanmate.puzzle :as puzzle]))

(defrecord Shape [name pieces])

(defn- shape [name piece-types]
  (let [[pieces _] (puzzle/layer-with-pieces piece-types)]
    (->Shape name pieces)))

(def four-four (shape "4-4" "eceeeeceee"))
(def five-three (shape "5-3" "eceeeeecee"))
(def six-two (shape "6-2" "ceeeeeecee"))
(def seven-one (shape "7-1" "ceeeeeeece"))
(def eight (shape "8" "ceeeeeeeec"))

(def two-two-two (shape "2-2-2" "eeceeceec"))
(def three-three (shape "3-3" "eecceeece"))
(def three-two-one (shape "3-2-1" "eeeceecec"))
(def three-one-two (shape "3-1-2" "ceeceeece"))
(def left-four-two (shape "Left 4-2" "ceeeeceec"))
(def right-four-two (shape "Right 4-2" "ceeceeeec"))
(def four-one-one (shape "4-1-1" "eceeeecec"))
(def left-five-one (shape "Left 5-1" "ceeeeecec"))
(def right-five-one (shape "Right 5-1" "ceceeeeec"))
(def six (shape "6" "ceeeeeecc"))

(def square (shape "Square" "cececece"))
(def kite (shape "Kite" "ceceecec"))
(def barrel (shape "Barrel" "ceecceec"))
(def shield (shape "Shield" "eeccceec"))
(def left-fist (shape "Left fist" "cececeec"))
(def right-fist (shape "Right fist" "ceececec"))
(def left-pawn (shape "Left pawn" "cceeecec"))
(def right-pawn (shape "Right pawn" "ceceeecc"))
(def mushroom (shape "Mushroom" "cceeecce"))
(def scallop (shape "Scallop" "cceeeecc"))

(def paired-edges (shape "Paired edges" "cccccee"))
(def perpendicular-edges (shape "Perpendicular edges" "ccccece"))
(def parallel-edges (shape "Parallel edges" "cccecce"))

(def star (shape "Star" "cccccc"))

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

(defn puzzle-with-layers [top-key bottom-key]
  (let [top-pieces (:pieces (get all-shapes top-key))
        bottom-pieces (:pieces (get all-shapes bottom-key))]
    (puzzle/->Puzzle (puzzle/->TopLayer top-pieces)
                     (puzzle/->BottomLayer bottom-pieces))))

(defn ordered-permutations
  "Given a layer's pieces returns a lazy seq of all the possible rotated states
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

(defn layer-shape [layer]
  (->> all-shapes
       (filter (fn [s]
                 (same-shape? (val s) layer)))
       first))

(defn layer-shape-name [layer]
  (:name (val (layer-shape layer))))

(defn layer-shape-name-key [layer]
  (key (layer-shape layer)))

(defn puzzle-layer-shape-names [puzzle]
  (let [top (layer-shape-name-key (:top-layer puzzle))
        bottom (layer-shape-name-key (:bottom-layer puzzle))]
    [top bottom]))

(defn same-shape-and-orientation? [layer-a layer-b]
  (= (puzzle/pieces-str layer-a)
     (puzzle/pieces-str layer-b)))
