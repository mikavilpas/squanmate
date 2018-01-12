(ns squanmate.alg.manipulation
  (:require [squanmate.alg.types :as types]
            [squanmate.alg.parser :as parser]
            [cats.core :as m]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [squanmate.alg.serialization :as serialization]
            [squanmate.alg.prettification :as prettification]
            [squanmate.alg.rotation :as rotation]))

(defn combine-rotations [a b]
  (-> a
      (update :top-amount #(prettification/prettify-value (+ % (:top-amount b))))
      (update :bottom-amount #(prettification/prettify-value (+ % (:bottom-amount b))))))

(defn append-final-rotation [rotation alg-steps]
  (if (= rotation/empty-rotation rotation)
    alg-steps
    (let [s (last alg-steps)]
      (cond
        (nil? rotation)
        alg-steps

        (= types/Rotations (type s))
        (let [new-last-step (combine-rotations s rotation)]
          (conj (vec (butlast alg-steps)) new-last-step))

        :else (conj (vec alg-steps) rotation)))))

(defn prepend-initial-rotation [rotation alg-steps]
  (if (= rotation/empty-rotation rotation)
    alg-steps
    (let [s (first alg-steps)]
      (cond
        (nil? rotation)
        alg-steps

        (= types/Rotations (type s))
        (let [new-first-step (combine-rotations s rotation)]
          (into [new-first-step] (rest alg-steps)))

        :else (into [rotation] alg-steps)))))

(defn negate-step [step]
  (if (= types/Slice (type step))
    step
    (-> step
        (update :top-amount -)
        (update :bottom-amount -))))

(defn reverse-steps [alg-steps]
  (->> alg-steps (map negate-step) reverse))

(defn flip-step-upside-down [step]
  (if (= types/Slice (type step))
    step
    (let [{:keys [top-amount bottom-amount]} step]
      (assoc step
             :top-amount bottom-amount
             :bottom-amount top-amount))))

(defn flip-alg-upside-down [alg-steps]
  (mapv flip-step-upside-down alg-steps))

;; includes the default, very opinionated error handler
(defn try-update-alg-string [alg-string update-alg-steps-fn]
  (either/branch (parser/parse alg-string)
                 (fn [error]
                   (js/console.log (str "error: cannot manipulate alg '"
                                        alg-string
                                        "'. Reason: "
                                        (pr-str error)))
                   ;; return the original so it isn't lost
                   alg-string)
                 (fn [alg-steps]
                   (-> alg-steps
                       update-alg-steps-fn
                       serialization/alg-to-str))))

(defn reverse-alg [alg-string]
  (try-update-alg-string alg-string reverse-steps))

(defn flip-alg-string-upside-down [alg-string]
  (try-update-alg-string alg-string flip-alg-upside-down))

(defn flip-initial-rotation-upside-down [alg-string]
  (try-update-alg-string
   alg-string
   (fn [alg-steps]
     (if (every? #(= (type %) types/Rotations)
                 alg-steps)
       (->> alg-steps
            (mapv flip-step-upside-down)
            (prepend-initial-rotation (types/->Rotations 6 6)))
       alg-string))))

(defn format-alg [alg-string]
  (try-update-alg-string alg-string identity))
