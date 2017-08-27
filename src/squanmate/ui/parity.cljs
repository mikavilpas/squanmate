(ns squanmate.ui.parity
  (:require [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes]
            [squanmate.alg.parity-counter :as parity-counter]
            [cats.monad.either :as either]
            [squanmate.puzzle :as puzzle]
            [clojure.string :as str]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

(defn- square-square? [puzzle]
  (let [layers (shapes/puzzle-layer-shape-names puzzle)
        result (= layers
                  ["square" "square"])]
    result))

(defn- parity-explanation []
  [:div "The " [:strong "parity count"] ", calculated with Cale Schoon's
    cubeshape parity method. In this method you determine the parity count at a
    specific starting position, and the apply either the " [:strong "even"] " or
    " [:strong "odd"] " algorithm."

   [:div.top10 "Note that if the count is odd it does not mean the algorithm
   necessarily switches parity! A different " [:strong "count position"] " might
   require switching the odd and even algorithms."]
   [:div.top10 "Find another count position using the " [:strong "initial
   rotation"] " controls."]])

(defn- parity-count-component [puzzle]
  (let [[parity? parity-data] (parity-counter/parity-count puzzle)]

    [common/overlay-trigger
     {:overlay (reagent/as-element [common/popover {:id "test"
                                                    :title "Parity count"}
                                    [parity-explanation]])
      :trigger "click"
      :placement "right"}
     (if parity?
       [common/button {:bs-style "info"
                       :bs-size "xsmall"} "Odd parity algorithm"]
       [common/button {:bs-style "warning"
                       :bs-size "xsmall"} "Even parity algorithm"])]))

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

(defn- alg-parity-switched-at-cubeshape? [alg-string]
  (m/mlet [steps (cubeshape-start-&-end-positions alg-string)
           start-step (either/right (first steps))
           end-step (either/right (last steps))]
          ;; double check - this is a precondition for calling this
          ;; component
          (if (= ["square" "square"]
                 (shapes/puzzle-layer-shape-names (:puzzle start-step)))
            (either/right [parity-count-component (:puzzle end-step)])
            (either/left "not at cubeshape"))))

(defn alg-parity-switched-at-cubeshape?-component
  ;; TODO comments can be moved to the UI in case we need to explain stuff to the user!

  "The parity of an algorithm is calculated like this:
  - start at the solved puzzle (obviously always at even parity)
  - apply the alg in reverse to get to the starting position
  - calculate the parity count for this specific position.

  If that count is even, the algorithm starts from even and ends with
  solved (always even), which means the algorithm preserves parity. Otherwise
  the algorithm is one that switches the parity once arriving at cubeshape.

  Should only be called when it's known that the alg-string leads into cubeshape
  from some known starting position.
  "
  [alg-string]
  (when (not (str/blank? alg-string))
    (either/branch (alg-parity-switched-at-cubeshape? alg-string)
                   #(println "could not determine parity of alg '" alg-string "': " %)
                   identity)))
