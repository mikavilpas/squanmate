(ns squanmate.drawing.monochrome
  (:require [reagent.core :as reagent]))

(defn- make-layer-url [layer]
  (let [size 100
        piece-letters (map :type (:pieces layer))]
    (str "http://localhost:9292/cubeshape/cececece"
         "?"
         "size=" size
         )))

(defn- layer-component [layer]
  [:img {:src (make-layer-url layer)}])

(defn monochrome-puzzle [puzzle]
  (let [top-img (layer-component (:top-layer puzzle))
        bottom-img (layer-component (:bottom-layer puzzle))]
    [:div.puzzle
     [:div.layer.top top-img]
     [:div.layer.bottom bottom-img]]))
