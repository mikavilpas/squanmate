(ns squanmate.services.cubeshape-piece-swapper
  (:require [squanmate.alg.puzzle :as p]
            [clojure.set :as set]))

(def piece-ids
  "Ids that correspond to the positions in an (aligned) square square puzzle.
  You can think of these as 'pointers' to the pieces in the actual puzzle. They
  are NOT the pieces themselves.

  For example,
  - in the solved puzzle, :ul-edge refers to the top-left piece
  - in the scramble (6,0), :ufl-corner refers to the top top-back-right piece

  These are in the same order as the solved puzzle has them. This is central to
  the design, and because of this, these ids cannot be used with misaligned or
  non-square-square shaped puzzles.
  "
  [;; top layer
   :ufl-corner :ul-edge
   :ulb-corner :ub-edge
   :ubr-corner :ur-edge
   :urf-corner :uf-edge

   ;; bottom layer
   :df-edge :dfr-corner
   :dr-edge :drb-corner
   :db-edge :dbl-corner
   :dl-edge :dlf-corner])

;; shorthands for callers
(def top-edges [:ul-edge
                :ub-edge
                :ur-edge
                :uf-edge])

(def ^:private position-indices (zipmap piece-ids (range)))

(defn- position [piece-id]
  (get position-indices piece-id))

(defn- piece-at [pieces piece-id]
  (get pieces (position piece-id)))

(defn- switch-piece-pair [pieces [from to]]
  (-> pieces
      (assoc (position from) (piece-at pieces to))
      (assoc (position to) (piece-at pieces from))))

(defn swap-pieces
  [puzzle substitutions-map]
  (p/update-pieces
   puzzle
   (fn [pieces]
     (reduce switch-piece-pair
             pieces
             substitutions-map))))
