(ns squanmate.ui.drawing.newmonochrome-test
  (:require [squanmate.ui.drawing.newmonochrome :as draw]
            [clojure.test :as t]
            [squanmate.puzzle :as p])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def cube-puzzle p/square-square)

(defcard-rg poc
  [:div
   (draw/monochrome-puzzle cube-puzzle)])
