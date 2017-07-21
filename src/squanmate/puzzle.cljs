(ns squanmate.puzzle
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

(defrecord Puzzle [top-layer bottom-layer])

;; the order of the pieces starts at the bottom left corner and goes clockwise
(defrecord TopLayer [pieces])
(defrecord BottomLayer [pieces])

(defrecord Piece [type colors])
(defrecord LayerError [msg layer])

;; edges will have a top color and one color,
;; corners will have one additional color
(defrecord PieceColors [top a b])

;; piece constructors
(defn- c [top a b] (->Piece "c" (->PieceColors top a b)))
(defn- edge [top a] (->Piece "e" (->PieceColors top a nil)))

(def square-square
  "This is in cube shape by default! This means slicing will go into kite kite."
  (Puzzle.
   (TopLayer. [(c ::top ::front ::left)
               (edge ::top ::left)
               (c ::top ::left ::back)
               (edge ::top ::back)
               (c ::top ::back ::right)
               (edge ::top ::right)
               (c ::top ::right ::front)
               (edge ::top ::front)])

   (BottomLayer. [(edge ::bottom ::left)
                  (c ::bottom ::front ::left)
                  (edge ::bottom ::back)
                  (c ::bottom ::left ::back)
                  (edge ::bottom ::right)
                  (c ::bottom ::back ::right)
                  (edge ::bottom ::front)
                  (c ::bottom ::right ::front)])))

(def ^:private all-pieces
  (vec (concat (:pieces (:top-layer square-square))
               (:pieces (:bottom-layer square-square)))))

(def ^:private corners (filterv #(= "c" (:type %)) all-pieces))
(def ^:private edges (filterv #(= "e" (:type %)) all-pieces))

(defn- take-piece [remaining-pieces piece-type]
  (let [p (first (get remaining-pieces piece-type))
        others (update remaining-pieces piece-type rest)]
    [p others]))

(defn layer-with-pieces
  "Constructs a layer by querying its pieces in the order given in `piece-types-str`.
  This has these benefits compared to constructing by hand:
  - guarantees the pieces' colors are not duplicated
  - layer definition is characterized by the layer's shape and not its colors
  "
  [piece-types-str]
  (loop [remaining-pieces {"c" corners, "e" edges}
         queried-piece-types piece-types-str
         result-pieces []]
    (if-let [q (first queried-piece-types)]
      (let [[p remaining] (take-piece remaining-pieces q)]
        (when (nil? p)
          (throw (new js/Error
                      (str "error: the layer '" piece-types-str "' is invalid"))))
        (recur remaining
               (rest queried-piece-types)
               (conj result-pieces p)))
      result-pieces)))

(defn piece-value [piece]
  (condp = (:type piece)
    "c" 2
    "e" 1
    (throw (str "unknown piece " (pr-str piece)))))

(defn pieces-str [layer]
  (->> layer
       :pieces
       (map :type)
       (apply str)))
