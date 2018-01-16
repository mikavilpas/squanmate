(ns squanmate.scramblers.algsets.case-by-case-algset
  (:require [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]
            [squanmate.alg.puzzle :as p]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.rotation :as rotation]
            [squanmate.alg.types :as types]
            [squanmate.alg.manipulation :as manipulation]))


(defn- rotate-square-layer
  "Rotates and keeps the expected orientation of the layer so scrambles can be
  generated from it."
  [layer]
  (rand-nth (rotation/possible-rotations
             layer
             [0 3 -3 6])))

(defn- apply-starting-rotation [puzzle]
  (letfn [(rotate [layer]
            (let [[new-layer _amount] (rotate-square-layer layer)]
              new-layer))]
    (-> puzzle
        (update :top-layer rotate)
        (update :bottom-layer rotate))))

(defn- prepend-random-rotations [alg-string]
  ;; note: the given alg-string must start at square-square
  (letfn [(random-rotation-amount [layer]
            (let [[_new-layer amount] (rotate-square-layer layer)]
              amount))]
    (let [top-amount (-> p/square-square :top-layer random-rotation-amount)
          bottom-amount (-> p/square-square :bottom-layer random-rotation-amount)
          rotations (types/->Rotations top-amount bottom-amount)]
      (manipulation/try-update-alg-string alg-string
                                          (partial manipulation/prepend-initial-rotation rotations)))))

(defn- add-initial-rotation-to-case [case]
  (let [[case-name alg] case
        rotated-alg (prepend-random-rotations alg)]
    [case-name rotated-alg]))

(defn- create-puzzle [case]
  ;; a starting rotation makes it possible to get the same EP case in all
  ;; different orientations
  (let [case (add-initial-rotation-to-case case)
        [case-name alg] case
        start (apply-starting-rotation p/square-square)
        scrambled-puzzle (->> alg
                              (execution/transformation-result-reverse start)
                              execution/puzzle-of-result)]
    scrambled-puzzle))

;; The default algset, which supports cases that have a fixed setup algorithm.
;; Examples of such are PLL and EP cases. Each of the cases is expected to be
;; solved with an algorithm memorized specifically for that case (without using
;; intuitive solving)
(defrecord CaseByCaseAlgSet [odd-cases even-cases]
  algset-scrambler/AlgSetScrambler
  (generate-puzzle [this case]
    (create-puzzle case)))
