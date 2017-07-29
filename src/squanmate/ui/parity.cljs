(ns squanmate.ui.parity
  (:require [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes]
            [squanmate.alg.parity-counter :as parity-counter]
            [cats.monad.either :as either]
            [squanmate.puzzle :as puzzle]
            [clojure.string :as str]))

(defn- square-square? [puzzle]
  (let [layers (shapes/puzzle-layer-shape-names puzzle)
        result (= layers
                  ["square" "square"])]
    result))

(defn- parity-count-component [puzzle]
  (let [[parity? parity-data] (parity-counter/parity-count puzzle)]
    [:div (if parity?
            "odd"
            "even")]))

(def misaligned-square-square (-> puzzle/square-square
                                  (execution/transformation-result "1,-1")
                                  m/extract
                                  :puzzle))

(defn- cubeshape-start-&-end-positions [alg-string]
  ;; Most cubeshape algs end up in either the positions 1,-1 or 0 (just the
  ;; solved puzzle). To make things easier, the user doesn't have to add a final
  ;; -1,1 to their algorithm in order to get a parity count.
  (let [steps1 (execution/transformations-reverse puzzle/square-square
                                                  alg-string)
        steps2 (execution/transformations-reverse misaligned-square-square
                                                  alg-string)]
    (if-let [successful-transformations
             (first (filter #(every? either/right? %)
                            [steps1 steps2]))]
      (either/right successful-transformations)
      (either/left "doesn't seem like a cubeshape algorithm"))))

(defn- alg-parity-switched-at-cubeshape? [alg-string]
  (m/mlet [step-eithers (cubeshape-start-&-end-positions alg-string)
          start-step-either (either/right (first step-eithers))
          end-step-either (either/right (last step-eithers))]
          (m/mlet [start-step start-step-either
                   end-step end-step-either]
                  (if (= ["square" "square"] (shapes/puzzle-layer-shape-names (:puzzle start-step)))
                    (either/right [parity-count-component (:puzzle end-step)])
                    (either/left "not at cubeshape")))))

(defn alg-parity-switched-at-cubeshape?-component
  ;; TODO comments can be moved to the UI in case we need to explain stuff to the user!

  "The parity of an algorithm is calculated like this:
  - start at the solved puzzle (obviously always at even parity)
  - apply the alg in reverse to get to the starting state
  - calculate the parity count for this specific state.

  If that count is even, the algorithm goes from even (start) -> even
  (solved, always even), which means the algorithm preserves parity.
  Otherwise the algorithm is one that switches the parity once arriving at cubeshape.
  "
  [alg-string]
  (when (not (str/blank? alg-string))
    (either/branch (alg-parity-switched-at-cubeshape? alg-string)
                   #(println "could not determine parity of alg '" alg-string "': " %)
                   identity)))
