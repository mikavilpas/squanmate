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
    (println (shapes/puzzle-layer-shape-names puzzle) [parity? parity-data])
    [:span (if parity?
             "odd"
             "even")]))

(defn- alg-parity-switched-at-cubeshape? [alg-string]
  (let [transformation-eithers (execution/transformations-reverse puzzle/square-square alg-string)
        starting-step-either (last transformation-eithers)]
    (m/mlet [start-transformation-step starting-step-either]
            (m/return [parity-count-component (:puzzle start-transformation-step)]))))

(defn alg-parity-switched-at-cubeshape?-component
  ;; TODO comments can be moved to the UI in case we need to explain stuff to the user!

  " Note: only call this if you know your `alg-string` is an algorithm that
  solves some state into cubeshape (square square). Otherwise you will get
  unreliable results!

  The parity of an algorithm is calculated like this:
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
                   #(println "could not determine parity of alg: " %)
                   identity)))
