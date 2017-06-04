(ns squanmate.ui.drawing.monochrome
  (:require [reagent.core :as reagent]))

(def ^:const size 100)

(defn- make-layer-url [layer]
  (let [piece-letters (map :type (:pieces layer))]
    (str "http://localhost:9292/cubeshape/"
         piece-letters
         "?"
         "size=" size)))

(defn- layer-component [layer]
  [:img {:src (make-layer-url layer)}])

(defn monochrome-puzzle [puzzle]
  (let [top-img (layer-component (:top-layer puzzle))
        bottom-img (layer-component (:bottom-layer puzzle))]
    [:div.puzzle
     [:div.layer.top top-img]
     [:div.layer.bottom {:style {:margin-top "-40px"}} bottom-img]]))
