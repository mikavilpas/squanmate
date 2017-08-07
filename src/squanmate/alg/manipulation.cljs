(ns squanmate.alg.manipulation
  (:require [squanmate.alg.types :as types]))

;; this is kind of lame, but at least it's super readable
(def ^:private prettifications {7 -5
                                8 -4
                                9 -3
                                10 -2
                                11 -1
                                12 0

                                -6 6
                                -7 5
                                -8 4
                                -9 3
                                -10 2
                                -11 1
                                -12 0})

(defn- prettify-value [n]
  (let [not-found n]
    (get prettifications n not-found)))

(defn combine-rotations [a b]
  (-> a
      (update :top-amount #(prettify-value (+ % (:top-amount b))))
      (update :bottom-amount #(prettify-value (+ % (:bottom-amount b))))))

(defn prepend-initial-rotation [rotation alg-steps]
  (let [s (first alg-steps)]
    (cond
      (nil? rotation)
      alg-steps

      (= types/Rotations (type s))
      (let [new-first-step (combine-rotations s rotation)]
        (into [new-first-step] (rest alg-steps)))

      :else (into [rotation] alg-steps))))

(defn reverse-steps [alg-steps]
  (reverse (for [step alg-steps]
             (if (= types/Slice (type step))
               step
               (-> step
                   (update :top-amount -)
                   (update :bottom-amount -))))))
