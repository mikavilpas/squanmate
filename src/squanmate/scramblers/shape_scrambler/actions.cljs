(ns squanmate.scramblers.shape-scrambler.actions
  (:require [squanmate.scramblers.shape-scrambler.default-scrambler
             :as
             default-scrambler]
            [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as pps]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.services.google-analytics :as ga]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.solving :as solving]))

(defonce all-layers (->> shape-combinations/possible-layers
                         (map set)
                         set))

(defn selected-shapes-count [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)
        percentage (-> (* 100 (/ layer-count 90))
                       (.toFixed 2))]
    [layer-count percentage]))

(defn selected-shapes-counter [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)
        percentage (-> (* 100 (/ layer-count 90))
                       (.toFixed 2))]
    [:div (str layer-count " / 90 total shapes selected (" percentage " %).")]))

(defn select-all-shapes [state]
  (swap! state assoc :selected-shapes all-layers))

(defn select-no-shapes [state]
  (swap! state assoc :selected-shapes #{}))

(defn new-scramble! [state scrambler]
  (let [[chosen-layers new-scramble] (scrambler/create-scramble scrambler)]
    (swap! state assoc
           :scramble-algorithm nil
           :puzzle new-scramble
           :chosen-shapes (into #{} chosen-layers))
    (solving/solve-and-generate-scramble new-scramble state)))

(defn set-new-scramble [state scrambler]
  (new-scramble! state scrambler)
  (ga/send-page-view :trainer/new-scramble))

(defn set-new-random-scramble [state]
  (let [s (default-scrambler/new-default-shape-scrambler (:selected-shapes @state))]
    (set-new-scramble state s)))

(defn set-new-repeat-scramble [state]
  (let [s (default-scrambler/new-default-shape-scrambler [(:chosen-shapes @state)])]
    (set-new-scramble state s)))

(defn set-new-scramble-with-parity [state relative-parity-type]
  (let [s (pps/->PredeterminedParityScrambler (:puzzle @state)
                                              relative-parity-type)]
    (new-scramble! state s)
    (ga/send-page-view :trainer/new-scramble)))
