(ns squanmate.ui.alg-importer
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.ui.common :as common]
            [squanmate.ui.parity :as parity]))

(defn starting-puzzle-for-alg [alg-string]
  (m/mlet [transformation-steps (parity/cubeshape-start-&-end-positions alg-string)]
          (m/return (-> transformation-steps last))))

(defn reverse-alg [alg-string]
  (if (empty? alg-string)
    (either/left "Algorithm not valid")
    (m/mlet [start-transformation-step (starting-puzzle-for-alg alg-string)]
            (let [alg (manipulation/reverse-alg alg-string)]
              (m/return [alg (:puzzle start-transformation-step)])))))

(defn- import-alg-component [alg-string]
  (let [result (reverse-alg alg-string)]
    [:div
     (either/branch
      result
      (fn [error]
        [common/alert {:bs-style :warning}
         error])
      (fn [success]
        [common/alert {:bs-style :success}
         "Success!"]))]))

(defn ui [state]
  (let [my-state (reagent/atom {:algorithm nil})]
    (fn [state]
      [:div
       [:h2 "Instructions:"]
       "Enter an algorithm that ends in cubeshape. Acceptable ending positions are (0) or (1,-1)."
       [common/input-box (reagent/cursor my-state [:algorithm])
        "Cubeshape algorithm"]
       [import-alg-component (:algorithm @my-state)]])))
