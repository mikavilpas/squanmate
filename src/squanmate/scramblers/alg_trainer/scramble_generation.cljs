(ns squanmate.scramblers.alg-trainer.scramble-generation
  (:require [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]
            [squanmate.services.google-analytics :as ga]
            [squanmate.services.solving :as solving]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.scramblers.algsets.permute-last-layer :as pll]
            [squanmate.scramblers.algsets.lin-corner-permutation :as lin-cp]
            [squanmate.scramblers.algsets.cubeshape :as cubeshape]))

(defn- set-scramble-for-start-position! [state puzzle]
  (swap! state assoc :puzzle puzzle)
  (solving/solve-and-generate-scramble puzzle state))

(defn- random-case [state]
  (let [cases (:selected-cases @state)
        case (rand-nth (seq cases))]
    case))

(defn- report-error-for-case [alg e]
  (js/console.log "Error setting up case for alg: " alg ". Description: " e))

(def algsets
  "The algorithm sets the algorithm trainer supports"
  [{:name "Cubeshape"
    :algset cubeshape/cubeshape-algset}
   {:name "Edge permutation (EP)"
    :algset ep/ep-algset}
   {:name "Permute last layer (PLL)"
    :algset pll/pll-algset}
   {:name "Lin corner permutation"
    :algset lin-cp/lin-cp-algset}])

(defn- algset-for-case [case]
  (first (filter (fn [algset]
                   (let [all-cases (algset-scrambler/all-cases algset)]
                     (some (partial = case) all-cases)))
                 (map :algset algsets))))

(defn new-scramble
  ([state]
   (new-scramble state (random-case state)))
  ([state case]
   (let [algset (algset-for-case case)]
     (assert algset (str "Internal error: algset not found for case " (pr-str case)))
     (let [puzzle (algset-scrambler/generate-puzzle algset case)]
       (swap! state assoc :scramble-algorithm ""
              :chosen-case case)
       (set-scramble-for-start-position! state puzzle)))))

(defn set-new-scramble! [& args]
  (apply new-scramble args)
  (ga/send-page-view :algorithm-trainer/new-scramble))
