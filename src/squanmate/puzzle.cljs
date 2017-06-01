(ns squanmate.puzzle)

;; the order of the pieces starts at the front equator, and go clockwise
(defrecord Layer [pieces])
(defrecord Piece [type])

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
