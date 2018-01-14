(ns squanmate.ui.parity
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.pages.links :as links]
            [squanmate.alg.puzzle :as puzzle]
            [squanmate.services.shapes :as shapes]
            [squanmate.ui.common :as common]
            [squanmate.services.alg-parity :as alg-parity]))

(defn- parity-explanation []
  [:div
   [:p "The " [:strong "parity count"] ", calculated with Cale Schoon's
    cubeshape parity method. In this method you determine the parity count at a
    specific starting position, and the apply either the " [:strong "even"] " or
    " [:strong "odd"] " algorithm."]

   [:p [common/help-block [common/glyphicon {:glyph :info-sign}]
        " The exact way the parity is counted is explained in "
        [:a {:href links/parity-alg-sheet-link
             :target "_blank"}
         "my cubeshape parity document"]]]

   [:p "Note that if the count is odd it does not mean the algorithm necessarily
   switches parity! A different " [:strong "count position"] " might require
   switching the odd and even algorithms."]

   [:p "Find another count position using the " [:strong "initial rotation"] "
   controls, or look at all possible count positions on the " [:strong "parity
   count positions"] " page."]])

(defn- parity-count-component [parity-count-result]
  (let [[parity? parity-data] parity-count-result]

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
    (either/branch (alg-parity/alg-parity-switched-at-cubeshape? alg-string)
                   #(println "could not determine parity of alg '" alg-string "': " %)
                   parity-count-component)))
