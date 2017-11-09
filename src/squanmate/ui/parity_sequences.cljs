(ns squanmate.ui.parity-sequences
  (:require [reagent.core :as reagent]
            [squanmate.ui.common :as common]
            [clojure.string :as str]
            [squanmate.services.parity-sequences :as parity-sequences]))

(defn- new-unique-sequence [state]
  (let [current-sequence (:parity-sequence @state)]
    (first (filter #(not (= current-sequence %))
                   (repeatedly parity-sequences/new-sequence)))))

(defn default-state []
  (reagent/atom {:parity-sequence (parity-sequences/new-sequence)}))

(defn- color [color-letter]
  [:div.color-box
   {:class (condp = color-letter
             "B" "blue"
             "O" "orange"
             "R" "red"
             "G" "green")}])

(defn- colors [colors-string]
  (when-not (str/blank? colors-string)
    (let [[a b c] colors-string]
      [:div.parity-sequence
       [color a]
       [color b]
       [color c]])))

(defn- new-question! [state]
  (swap! state assoc :parity-sequence
         (new-unique-sequence state)))

(defn- has-parity? [parity-sequence]
  (let [s (apply str parity-sequence)
        res (parity-sequences/parity? s)
        parity? (not (nil? res))]
    parity?))

(defn answer! [parity? state]
  (when (= parity?
           (has-parity? (:parity-sequence @state)))
    (new-question! state)))

(defn- buttons [state]
  [:div.center.top17
   [common/button {:bs-size "large"
                   :on-click #(answer! true state)}
    "Odd"]
   [common/button {:bs-size "large"
                   :on-click #(answer! false state)}
    "Even"]])

(defn parity-game [state]
  [:div
   [colors (:parity-sequence @state)]
   [buttons state]])
