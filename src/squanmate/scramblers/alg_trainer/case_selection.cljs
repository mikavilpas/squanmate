(ns squanmate.scramblers.alg-trainer.case-selection
  (:require [clojure.set :as set]))

(defn case-selected? [state case]
  (contains? (:selected-cases @state) case))

(defn select-or-deselect! [state case]
  (if (case-selected? state case)
    (swap! state update :selected-cases disj case)
    (swap! state update :selected-cases conj case)))

(defn select-cases! [state cases]
  (swap! state update :selected-cases into cases))

(defn deselect-cases! [state cases]
  (swap! state update :selected-cases #(set/difference % (set cases))))
