(ns squanmate.ui.inspection-timer-test
  (:require [squanmate.ui.inspection-timer :as sut])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def from-start (sut/new-count-down-timer 15))
(defcard-rg timer-from-the-beginning
  [sut/inspection-timer from-start]
  from-start
  {:inspect-data true})

(defn timer-at-seconds [seconds]
  (let [s (sut/new-count-down-timer 15)]
    (swap! s assoc
           :started? true
           :completed? false
           :elapsed-seconds seconds)
    s))

(defcard-rg timer-ongoing
  (into [:div]
        (for [s (range 0 16)]
          [:div {:style {:padding 1}}
           [sut/inspection-timer (timer-at-seconds s)]])))
