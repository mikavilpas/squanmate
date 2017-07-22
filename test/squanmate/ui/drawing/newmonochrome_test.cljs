(ns squanmate.ui.drawing.newmonochrome-test
  (:require [squanmate.ui.drawing.newmonochrome :as draw]
            [clojure.test :as t]
            [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def sample-puzzle (p/Puzzle. (p/TopLayer. (:pieces shapes/barrel))
                              (p/BottomLayer. (:pieces shapes/shield))))

(defcard-rg poc
  [:div
   (draw/monochrome-puzzle sample-puzzle)])
