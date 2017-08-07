(ns squanmate.scramblers.shape-scrambler
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn scramble []
  (let [top (shape-str "square")
        bottom (shape-str "square")]
    (println "generating scramble:")
    (println top)
    (println bottom)
    (p/puzzle-with-shapes top bottom)))
