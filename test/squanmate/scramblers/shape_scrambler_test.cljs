(ns squanmate.scramblers.shape-scrambler-test
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [clojure.test :as t :refer [is]]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defcard-rg scramble-preview
  [newmonochrome/monochrome-puzzle (shape-scrambler/scramble)
   {:monochrome? false}])
