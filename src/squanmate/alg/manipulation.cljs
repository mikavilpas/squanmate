(ns squanmate.alg.manipulation
  (:require [squanmate.alg.types :as types]
            [squanmate.alg.parser :as parser]
            [cats.core :as m]
            [squanmate.puzzle :as puzzle]))

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

(defn prettify-value [n]
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

(defn- negate-step [step]
  (if (= types/Slice (type step))
    step
    (-> step
        (update :top-amount -)
        (update :bottom-amount -))))

(defn reverse-steps [alg-steps]
  (->> alg-steps (map negate-step) reverse))

(defn- flip-step-upside-down [step]
  (if (= types/Slice (type step))
    step
    (let [{:keys [top-amount bottom-amount]} step]
      (assoc step
             :top-amount bottom-amount
             :bottom-amount top-amount))))

(defn flip-alg-upside-down [alg-steps]
  (->> alg-steps
       (mapv flip-step-upside-down)
       (map negate-step)))
