(ns squanmate.ui.drawing.monochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [clojure.string :as string]))

(def ^:const size 100)

;; Note: cubeshape is the app that draws the puzzle. So this is for building a
;; cubeshape app specific request string.
(defprotocol CubeshapePiecesString
  (cubeshape-app-string [layer]))

(extend-type p/TopLayer
  CubeshapePiecesString
  (cubeshape-app-string [layer]
    (p/pieces-str layer)))

(extend-type p/BottomLayer
  CubeshapePiecesString
  (cubeshape-app-string [layer]
    (let [[left right] (slicing/split-at-6 layer)
          left-string (string/join (map :type left))
          right-string (string/join (map :type right))
          layer-string (str left-string right-string)]
      layer-string)))

(defn- make-layer-url [layer]
  (str "http://localhost:9292/cubeshape/"
       (cubeshape-app-string layer)
       "?"
       "size=" size))

(defn- layer-component [layer]
  [:img {:src (make-layer-url layer)}])

(defn monochrome-puzzle [puzzle & debug?]
  (let [top-img (layer-component (:top-layer puzzle))
        bottom-img (layer-component (:bottom-layer puzzle))]
    [:div.puzzle
     [:span.layer.top top-img]
     [:span.layer.bottom {:style {:margin-left "-20px"}} bottom-img]
     (when debug?
       [:div
        "Top:" (p/pieces-str (:top-layer puzzle))
        ", "
        "Bottom: " (p/pieces-str (:bottom-layer puzzle))])]))
