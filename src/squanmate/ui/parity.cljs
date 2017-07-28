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
    [:span (if parity?
             "odd"
             "even")]))

(def misaligned-square-square (-> puzzle/square-square
                                  (execution/transformation-result "1,-1")
                                  m/extract
                                  :puzzle))

(defn- final-cubeshape-position [alg-string]
  ;; Most cubeshape algs end up in either the positions 1,-1 or 0 (just the
  ;; solved puzzle). To make things easier, the user doesn't have to add a final
  ;; -1,1 to their algorithm in order to get a parity count.
  (let [last-step (execution/transformation-result-reverse puzzle/square-square
                                                           alg-string)
        last-step2 (execution/transformation-result-reverse misaligned-square-square
                                                            alg-string)
        both [last-step last-step2]]
    (or (either/first-right both)
        (either/first-left both))))

(defn- alg-parity-switched-at-cubeshape? [alg-string]
  (let [starting-step-either (final-cubeshape-position alg-string)]
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
