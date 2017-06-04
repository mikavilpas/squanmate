(ns squanmate.slicing
  (:require [squanmate.puzzle :as p]
            [cats.monad.either :as either]))

(defn layer-sliceable? [layer]
  ;; Either the top or bottom layer can be sliced if it has an edge of a piece
  ;; at the slice point. The slice always happens at a distance of 6.
  (let [values (map p/piece-value (:pieces layer))
        sums (reductions + values)]
    (some (partial = 6) sums)))

;; always slices the right hand side
(defprotocol Sliceable
  (slice-and-static-pieces [layer]))

(defn split-at-6 [layer]
  (loop [taken-pieces []
         remaining-pieces (:pieces layer)
         collected-pieces-value 0]
    (if (= collected-pieces-value 6)
      [taken-pieces remaining-pieces]
      (let [this-piece (first remaining-pieces)]
        (recur (conj taken-pieces this-piece)
               (next remaining-pieces)
               (+ collected-pieces-value
                  (p/piece-value this-piece)))))))

(extend-type p/TopLayer
  Sliceable
  (slice-and-static-pieces [layer]
    (let [[left right] (split-at-6 layer)]
      ;; slice on the right
      [right left])))

(extend-type p/BottomLayer
  Sliceable
  (slice-and-static-pieces [layer]
    (let [[left right] (split-at-6 layer)]
      [left right])))

(defn- do-slice [puzzle]
  (let [[top-slice-pieces
         top-static-pieces] (slice-and-static-pieces (:top-layer puzzle))
        [bottom-slice-pieces
         bottom-static-pieces] (slice-and-static-pieces (:bottom-layer puzzle))
        new-top-layer (assoc-in (:top-layer puzzle)
                                [:pieces]
                                (vec (concat top-static-pieces
                                             bottom-slice-pieces)))
        new-bottom-layer (assoc-in (:bottom-layer puzzle)
                                   [:pieces]
                                   (vec (concat (reverse top-slice-pieces)
                                                (reverse bottom-static-pieces))))]
    (-> puzzle
        (assoc-in [:top-layer] new-top-layer)
        (assoc-in [:bottom-layer] new-bottom-layer)
        either/right)))

(defn slice [puzzle]
  (cond
    (->> puzzle :top-layer layer-sliceable? not)
    (either/left (p/LayerError. "cannot slice, because the top layer is not aligned"
                                (:top-layer puzzle)))

    (->> puzzle :bottom-layer layer-sliceable? not)
    (either/left (p/LayerError. "cannot slice, because the bottom layer is not aligned"
                                (:bottom-layer puzzle)))

    :else (do-slice puzzle)))
