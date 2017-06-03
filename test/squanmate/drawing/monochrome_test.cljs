(ns squanmate.drawing.monochrome-test
  (:require [squanmate.drawing.monochrome :as draw]
            [clojure.test :as t]
            [squanmate.puzzle :as p])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def cube-puzzle (p/Puzzle. p/square-layer p/square-layer))

(defcard-rg poc
  (draw/monochrome-puzzle cube-puzzle))
