(ns squanmate.scramblers.shape-scrambler.actions
  (:require [squanmate.scramblers.shape-scrambler.default-scrambler :as default-scrambler]
            [squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler :as pps]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.services.google-analytics :as ga]
            [squanmate.services.shape-combinations :as shape-combinations]
            [squanmate.services.solving :as solving]
            [clojure.set :as set]
            [squanmate.services.storage :as storage]))

(defonce all-layers (->> shape-combinations/possible-layers
                         (map set)
                         set))

(defn no-cases-selected? [state]
  (let [selected-layers-count (-> @state :selected-shapes count)]
    (<= selected-layers-count 0)))

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

(defn set-specific-shapes-scramble [state shapes-sets]
  (let [s (default-scrambler/new-default-shape-scrambler shapes-sets)]
    (set-new-scramble state s)))

(defn set-new-repeat-scramble [state]
  (set-specific-shapes-scramble state [(:chosen-shapes @state)]))

(defn set-new-scramble-with-parity [state relative-parity-type]
  (let [s (pps/->PredeterminedParityScrambler (:puzzle @state)
                                              relative-parity-type)]
    (new-scramble! state s)
    (ga/send-page-view :trainer/new-scramble)))

(defn deselect-case-and-generate-new-scramble! [state]
  (let [this-case (:chosen-shapes @state)]
    (swap! state update :selected-shapes set/difference #{this-case}))
  (set-new-random-scramble state))
