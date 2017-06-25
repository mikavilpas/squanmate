(ns squanmate.ui.drawing.monochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [clojure.string :as string]
            [squanmate.shapes :as shapes]))

;; Note: cubeshape is the app that draws the puzzle. So this is for building a
;; cubeshape app specific request string.
(defprotocol CubeshapePiecesString
  (cubeshape-app-string [layer]))

(extend-protocol CubeshapePiecesString
  shapes/Shape
  (cubeshape-app-string [shape]
    (p/pieces-str shape))

  p/TopLayer
  (cubeshape-app-string [top-layer]
    (p/pieces-str top-layer))

  p/BottomLayer
  (cubeshape-app-string [bottom-layer]
    (let [[left right] (slicing/split-at-6 bottom-layer)
          left-string (string/join (map :type left))
          right-string (string/join (map :type right))
          layer-string (str left-string right-string)]
      layer-string)))

(defn layer-component [layer & {:keys [size]
                                :or {size 100}}]
  (when layer
    (let [url (str "http://localhost:9292/cubeshape/"
                   (cubeshape-app-string layer)
                   "?"
                   "size=" size)]
      [:img {:src url}])))

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
