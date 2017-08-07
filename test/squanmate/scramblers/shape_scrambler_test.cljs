(ns squanmate.scramblers.shape-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler])
  (:require-macros [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg scramble-preview
  (let [p (shape-scrambler/scramble)]
    [shape-scrambler/scramble-component p]))
