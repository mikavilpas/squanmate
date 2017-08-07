(ns squanmate.scramblers.shape-scrambler
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.solving :as solving]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.serialization :as serialization]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cats.core :as m]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn scramble []
  (let [top (shape-str "square")
        bottom (shape-str "square")]
    (p/puzzle-with-shapes top bottom)))

(defn scramble-component [p]
  (let [solution-atom (solving/solve p)]
    (fn [p]
      (let [solution-alg @solution-atom
            steps (m/extract (parser/parse solution-alg))
            reverse-steps (manipulation/reverse-steps steps)
            scramble-string (serialization/alg-to-str reverse-steps)]
        [:div
         [newmonochrome/monochrome-puzzle p
          {:monochrome? false}]
         (str "Scramble: " scramble-string)]))))
