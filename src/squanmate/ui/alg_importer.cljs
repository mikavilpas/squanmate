(ns squanmate.ui.alg-importer
  (:require [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent]
            [squanmate.alg.parser :as parser]
            [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as puzzle]
            [squanmate.ui.parity :as parity]))

(defn starting-puzzle-for-alg [alg-string]
  (m/mlet [transformation-steps (parity/cubeshape-start-&-end-positions alg-string)]
          (-> transformation-steps last :puzzle)))

(defn reverse-alg [alg-string]
  (m/mlet [starting-step (starting-puzzle-for-alg alg-string)]))

(defn ui [state]
  (let [my-state (reagent/atom {:algorithm nil})]
    (fn [state]
      [:div
       [common/input-box (reagent/cursor my-state [:algorithm])
        "Algorithm"]
       ])))
