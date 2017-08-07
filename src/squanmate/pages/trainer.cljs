(ns squanmate.pages.trainer
  (:require [reagent.core :as reagent]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]))

(defonce state (reagent/atom
                {:puzzle (shape-scrambler/scramble)}))

(defn content []
  [shape-scrambler/scramble-component (:puzzle @state)])
