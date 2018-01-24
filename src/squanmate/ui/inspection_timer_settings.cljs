(ns squanmate.ui.inspection-timer-settings
  (:require [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

(defn default-state []
  (reagent/atom {:show-inspection-timer? false}))

(defn inspection-timer-options [state]
  [common/form-group
   [common/control-label "Inspection timer"]
   [common/checkbox {:checked (-> @state :show-inspection-timer?)
                     :on-change #(swap! state update :show-inspection-timer? not)}
    "Show a timer with 15 seconds of inspection time"]])
