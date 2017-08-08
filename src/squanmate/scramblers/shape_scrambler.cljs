(ns squanmate.scramblers.shape-scrambler
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.solving :as solving]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.serialization :as serialization]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cats.core :as m]
            [squanmate.ui.common :as common]
            [squanmate.shape-combinations :as shape-combinations]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn scramble []
  (let [[top-name bottom-name] (rand-nth shape-combinations/possible-layers)
        top (shape-str top-name)
        bottom (shape-str bottom-name)]
    (p/puzzle-with-shapes top bottom)))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn scramble-component [p]
  (let [solution-atom (solving/solve p)]
    (fn [p]
      (let [solution-alg @solution-atom
            steps (m/extract (parser/parse solution-alg))
            reverse-steps (manipulation/reverse-steps steps)
            scramble-string (serialization/alg-to-str reverse-steps)]
        [:div
         [:div.center
          [newmonochrome/monochrome-puzzle p
           {:monochrome? false
            :size 180}]]
         [:div.center
          [scramble-preview scramble-string]]]))))
