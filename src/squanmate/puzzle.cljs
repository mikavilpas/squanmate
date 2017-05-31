(ns squanmate.puzzle)

(defrecord Piece [type])
(defrecord Layer [pieces])

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
    "c" 1))

(defn layer-sliceable? [layer]
  )
