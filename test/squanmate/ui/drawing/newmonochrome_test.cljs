(ns squanmate.ui.drawing.newmonochrome-test
  (:require [squanmate.ui.drawing.newmonochrome :as draw]
            [clojure.test :as t]
            [squanmate.puzzle :as p]
            [squanmate.services.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def sample-puzzle (shapes/puzzle-with-layers "barrel" "shield"))

(defcard-rg poc
  [:div
   (draw/monochrome-puzzle sample-puzzle)])
