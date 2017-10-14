(ns squanmate.scramblers.alg-trainer.case-selection
  (:require [clojure.set :as set]))

(defn- case-selected? [state case-name]
  (contains? (:selected-cases @state) case-name))

(defn- select-or-deselect! [state case-name]
  (if (case-selected? state case-name)
    (swap! state update :selected-cases disj case-name)
    (swap! state update :selected-cases conj case-name)))

(defn- case-names [cases]
  (map (fn [[name alg]] name)
       cases))

(defn- select-cases! [state cases]
  (swap! state update :selected-cases into (case-names cases)))

(defn- deselect-cases! [state cases]
  (swap! state update :selected-cases #(set/difference % (case-names cases))))
