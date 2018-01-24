(ns squanmate.ui.inspection-timer
  (:require [cljsjs.tock]
            [reagent.core :as reagent]
            [squanmate.ui.common :as common]
            [squanmate.utils.hiccup-utils :as hu]))

(defn- count-full-seconds [state tock start-seconds]
  (let [elapsed-ms (.lap tock)
        remaining-seconds (-> elapsed-ms
                              (/ 1000)
                              js/Math.ceil)
        elapsed-seconds (-> remaining-seconds
                            (- start-seconds)
                            js/Math.abs)]
    (swap! state assoc
           :remaining-seconds remaining-seconds
           :elapsed-seconds elapsed-seconds)))

(defn- set-completed [state tock]
  (swap! state assoc :completed? true)
  (.stop tock))

(defn- start [tock start-ms state]
  (.start tock start-ms)
  (swap! state assoc :started? true))

(defn new-count-down-timer
  "A timer that counts down from the given amount of seconds.

  Returns a Reagent atom with an easily usable state for Reagent components to
  use."
  [start-seconds]
  ;; Setting up the timer and comverting it to a reagent atom is a chicken-egg
  ;; problem. I found no nice way to do it descriptively, so I do it by using
  ;; mutation.
  (let [tock (new js/Tock (clj->js {:countdown true}))

        start-ms (* 1000 start-seconds)
        state (reagent/atom {:remaining-seconds start-seconds
                             :elapsed-seconds 0
                             :completed? false
                             :started? false})]
    (swap! state assoc :start-fn #(start tock start-ms state))
    (aset tock "callback"
          #(count-full-seconds state tock start-seconds))
    (aset tock "complete"
          #(set-completed state tock))

    state))

(defn- time-display [timer]
  [:div
   (:elapsed-seconds @timer)])

(defn- running [timer]
  [time-display timer])

(defn- waiting [timer]
  [:div.waiting "(Click to start inspection)"])

(defn- timer-class [timer]
  (let [t (:elapsed-seconds @timer)]
    (cond
      (>= t 15)
      "fifteen-seconds"

      (>= t 14)
      "fourteen-seconds"

      (>= t 12)
      "twelve-seconds"

      (>= t 8)
      "eight-seconds"

      :else
      "")))

(defn inspection-timer [timer]
  [:button.flat.timer {:on-click (:start-fn @timer)
                       :class (timer-class timer)}
   (if (:started? @timer)
     [running timer]
     [waiting timer])])
