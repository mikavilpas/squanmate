(ns squanmate.alg.manipulation
  (:require [squanmate.alg.types :as types]))

(defn- combine-rotations [a b]
  (-> a
      (update :top-amount #(+ % (:top-amount b)))
      (update :bottom-amount #(+ % (:bottom-amount b)))))

(defn prepend-initial-rotation [rotation alg-steps]
  (let [s (first alg-steps)]
    (cond
      (nil? rotation)
      alg-steps

      (= types/Rotations (type s))
      (let [new-first-step (combine-rotations s rotation)]
        (into [new-first-step] (rest alg-steps)))

      :else (into [rotation] alg-steps))))
