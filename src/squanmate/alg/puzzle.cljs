(ns squanmate.alg.puzzle)

(defrecord Puzzle [top-layer bottom-layer])

;; the order of the pieces starts at the bottom left corner and goes clockwise
(defrecord TopLayer [pieces])
(defrecord BottomLayer [pieces])

(defrecord Piece [type colors])
(defrecord LayerError [msg layer])

;; edges will have a top color and one color,
;; corners will have one additional color
(defrecord PieceColors [top a b])

(defn corner? [p] (= "c" (:type p)))
(defn edge? [p] (= "e" (:type p)))
(defn top-piece? [p] (= :top (-> p :colors :top)))
(defn bottom-piece? [p] (= :bottom (-> p :colors :top)))

;; piece constructors
(defn- c [top a b] (->Piece "c" (->PieceColors top a b)))
(defn- edge [top a] (->Piece "e" (->PieceColors top a nil)))

(def square-square
  "This is in cube shape by default! This means slicing will go into kite kite."
  (Puzzle.
   (TopLayer. [(c :top :front :left)
               (edge :top :left)
               (c :top :left :back)
               (edge :top :back)
               (c :top :back :right)
               (edge :top :right)
               (c :top :right :front)
               (edge :top :front)])

   (BottomLayer. [(edge :bottom :front)
                  (c :bottom :front :right)
                  (edge :bottom :right)
                  (c :bottom :right :back)
                  (edge :bottom :back)
                  (c :bottom :back :left)
                  (edge :bottom :left)
                  (c :bottom :left :front)])))

(def ^:private all-pieces
  "random on each initiation (typically a page reload)"
  (vec (shuffle (concat (:pieces (:top-layer square-square))
                        (:pieces (:bottom-layer square-square))))))

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
  ([piece-types-str]
   (layer-with-pieces {"c" corners, "e" edges} piece-types-str))

  ([available-pieces piece-types-str]
   (loop [remaining-pieces available-pieces
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
       [result-pieces remaining-pieces]))))

(defn puzzle-with-shapes [top-layer-piece-types
                          bottom-layer-piece-types]
  (let [pieces {"c" (shuffle corners)
                "e" (shuffle edges)}
        [top-layer pieces] (layer-with-pieces pieces
                                              top-layer-piece-types)

        [bottom-layer pieces] (layer-with-pieces pieces
                                                 bottom-layer-piece-types)]
    (->Puzzle (->TopLayer top-layer) (->BottomLayer bottom-layer))))

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

(defn puzzle-pieces [puzzle]
  (vec (concat (-> puzzle :top-layer :pieces)
               (-> puzzle :bottom-layer :pieces))))

(defn layer-pieces-by-type [layer]
  (let [pieces (:pieces layer)
        corners (filterv corner? pieces)
        edges (filterv edge? pieces)]
    [corners edges]))

(defn update-pieces [puzzle update-fn]
  ;; functor like interface: update contents while preserving the structure
  (let [all-pieces (into (-> puzzle :top-layer :pieces)
                         (-> puzzle :bottom-layer :pieces))
        update-results (update-fn all-pieces)

        top-count (count (-> puzzle :top-layer :pieces))
        new-top-layer-pieces (->> update-results (take top-count) vec)
        new-bottom-layer-pieces (->> update-results (drop top-count) vec)]

    (-> puzzle
        (assoc-in [:top-layer :pieces] new-top-layer-pieces)
        (assoc-in [:bottom-layer :pieces] new-bottom-layer-pieces))))
