(ns squanmate.ui.parity
  (:require [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.shapes :as shapes]
            [squanmate.alg.parity-counter :as parity-counter]
            [cats.monad.either :as either]))

(defn- square-square? [puzzle]
  (let [layers (shapes/puzzle-layer-shape-names puzzle)
        result (= layers
                  ["square" "square"])]
    (println "layers: " layers)
    result))

(defn- parity-count-component [puzzle]
  (let [[parity? parity-data] (parity-counter/parity-count puzzle)]
    (println [parity? parity-data])
    [:span (if parity?
             "odd"
             "even")]))

(defn alg-parity-switched?-component [transformation-eithers]
  (let [result-component (m/mlet [start-alg-state (first transformation-eithers)
                                  finished-alg-state (last transformation-eithers)]
                                 (let [start (:puzzle start-alg-state)
                                       finish (:puzzle finished-alg-state)]
                                   (m/return
                                    (when (square-square? finish)
                                      [:div
                                       [:div "Parity count at start: " [parity-count-component start]]
                                       [:div "Parity count at finish: " [parity-count-component finish]]]))))]
    (either/branch result-component
                   (fn [error]
                     (println "could not determine if the parity has changed due to an error"))
                   identity)))
