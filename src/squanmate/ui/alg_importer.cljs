(ns squanmate.ui.alg-importer
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.ui.common :as common]
            [squanmate.ui.parity :as parity]
            [squanmate.alg.serialization :as serialization]
            [squanmate.shapes :as shapes]
            [clojure.string :as str]))

(defn starting-puzzle-for-alg [alg-string]
  (m/mlet [transformation-steps (parity/cubeshape-start-&-end-positions alg-string)]
          (m/return (-> transformation-steps last))))

(defn import-alg [alg-string]
  (m/mlet [start-transformation-step (starting-puzzle-for-alg alg-string)]
          (let [puzzle-spec (-> start-transformation-step
                                :puzzle
                                serialization/puzzle-specification)
                reversed-alg (manipulation/reverse-alg alg-string)]
            (m/return {:reversed-alg reversed-alg
                       :starting-puzzle-spec puzzle-spec}))))

(defn- import-alg-component [alg-string]
  (when-not (str/blank? alg-string)
    (let [result (import-alg alg-string)]
      [:div
       (either/branch
        result
        (fn [error]
          [common/alert {:bs-style :warning}
           error])
        (fn [success]
          [common/alert {:bs-style :success}
           "Success!"]))])))

(defn ui [state]
  (let [my-state (reagent/atom {:algorithm nil})]
    (fn [state]
      [:div
       [:h2 "Instructions:"]
       "Use this if you want to inspect an algorithm with Squanmate."
       [:div
        "Enter an algorithm that ends in cubeshape. Acceptable ending positions are (0) or (1,-1)."]
       [common/input-box (reagent/cursor my-state [:algorithm])
        "Cubeshape algorithm"]
       [import-alg-component (:algorithm @my-state)]])))
