(ns squanmate.slicing
  (:require [squanmate.puzzle :as p]
            [cats.monad.either :as either]))

(defn pieces-and-their-positions [layer]
  ;; to get the pieces' positions return the piece count before each piece
  (let [pieces (:pieces layer)
        successive-value-count (->> (reverse pieces)
                                    (map p/piece-value)
                                    (reductions - 12)
                                    reverse)]
    ;; return pairs like [piece, index]
    (partition 2
               (interleave pieces
                           successive-value-count))))

(defn layer-sliceable? [layer]
  ;; Either the top or bottom layer can be sliced if it has an edge of a piece
  ;; at the slice point. The slice always happens at a distance of 6.
  (let [values (map p/piece-value (:pieces layer))
        sums (reductions + values)]
    (some (partial = 6) sums)))

(defn split-at-6 [layer]
  (loop [left-pieces []
         remaining-pieces (vec (:pieces layer))
         collected-pieces-value 0]
    (if (= collected-pieces-value 6)
      [left-pieces remaining-pieces]
      (let [this-piece (first remaining-pieces)]
        (recur (conj left-pieces this-piece)
               (next remaining-pieces)
               (+ collected-pieces-value
                  (p/piece-value this-piece)))))))

(defn- do-slice [puzzle]
  (let [[top-static-pieces
         top-slice-pieces] (split-at-6 (:top-layer puzzle))
        [bottom-slice-pieces
         bottom-static-pieces] (split-at-6 (:bottom-layer puzzle))
        new-top-layer (assoc-in (:top-layer puzzle)
                                [:pieces]
                                (vec (concat top-static-pieces
                                             bottom-slice-pieces)))
        new-bottom-layer (assoc-in (:bottom-layer puzzle)
                                   [:pieces]
                                   (vec (concat top-slice-pieces
                                                bottom-static-pieces)))]
    (-> puzzle
        (assoc-in [:top-layer] new-top-layer)
        (assoc-in [:bottom-layer] new-bottom-layer)
        either/right)))

(defn top-sliceable? [puzzle]
  (->> puzzle :top-layer layer-sliceable?))

(defn bottom-sliceable? [puzzle]
  (->> puzzle :bottom-layer layer-sliceable?))

;; always slices the right hand side
(defn slice [puzzle]
  (cond

    ;; validation
    (not (top-sliceable? puzzle))
    (either/left (p/LayerError. "cannot slice, because the top layer is not aligned"
                                (:top-layer puzzle)))

    (not (bottom-sliceable? puzzle))
    (either/left (p/LayerError. "cannot slice, because the bottom layer is not aligned"
                                (:bottom-layer puzzle)))

    :else (do-slice puzzle)))

(defn sliceable? [puzzle]
  (and (top-sliceable? puzzle)
       (bottom-sliceable? puzzle)))
