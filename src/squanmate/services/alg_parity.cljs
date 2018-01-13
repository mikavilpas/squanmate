(ns squanmate.services.alg-parity
  "Functions for defining whether a given algorithm swaps parity or not."
  (:require [squanmate.puzzle :as puzzle]
            [squanmate.alg.execution :as execution]
            [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.services.shapes :as shapes]
            [squanmate.alg.parity-counter :as parity-counter]))

(def misaligned-square-square (-> puzzle/square-square
                                  (execution/transformation-result "1,-1")
                                  m/extract
                                  :puzzle))

(defn cubeshape-start-&-end-positions [alg-string]
  ;; Most cubeshape algs end up in either the positions 1,-1 or 0 (just the
  ;; solved puzzle). To make things easier, the user doesn't have to add a final
  ;; -1,1 to their algorithm in order to get a parity count.
  (let [steps1 (execution/transformations-reverse puzzle/square-square
                                                  alg-string)
        steps2 (execution/transformations-reverse misaligned-square-square
                                                  alg-string)
        first-successful-steps (->> [steps1 steps2]
                                    (filter #(every? either/right? %))
                                    first)
        successful-transformations (mapv m/extract first-successful-steps)]
    (if-not (empty? successful-transformations)
      (either/right successful-transformations)
      (either/left "Doesn't seem like a cubeshape algorithm"))))

(defn alg-parity-switched-at-cubeshape? [alg-string]
  (m/mlet [steps (cubeshape-start-&-end-positions alg-string)
           start-step (either/right (first steps))
           end-step (either/right (last steps))]
          ;; double check - this is a precondition for calling this
          ;; function
          (if (= ["square" "square"]
                 (shapes/puzzle-layer-shape-names (:puzzle start-step)))
            (either/right (parity-counter/parity-count (:puzzle end-step)))
            (either/left "not at cubeshape"))))
