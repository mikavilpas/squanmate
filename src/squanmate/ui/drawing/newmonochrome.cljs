(ns squanmate.ui.drawing.newmonochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [cljsjs.react-bootstrap]
            [squanmate.ui.drawing.util.quil-reagent :as quil-reagent]
            [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.pieces :as pieces]))

(defprotocol Drawable
  (draw [layer]))

(extend-protocol Drawable
  shapes/Shape
  (draw [shape]
    #'pieces/draw-top-layer)

  p/TopLayer
  (draw [top-layer]
    #'pieces/draw-top-layer)

  p/BottomLayer
  (draw [bottom-layer]
    #'pieces/draw-bottom-layer))

(defn layer-component [layer & {:keys [size]
                                :or {size 100}}]
  (let [draw-function-var (draw layer)
        shape-name (shapes/layer-shape-name layer)]
    [common/overlay-trigger
     {:overlay (reagent/as-element [common/tooltip {:id "test"}
                                    shape-name])
      :placement "top"}
     [:div {:style { "display" "inline-block" }}
      [quil-reagent/sketch
       :setup (pieces/setup layer size)
       :draw draw-function-var
       :update (pieces/update-sketch)
       :middleware [m/fun-mode]
       :size [size size]]]]))

(defn monochrome-puzzle [puzzle & debug?]
  (let [top-canvas (layer-component (:top-layer puzzle))
        bottom-canvas (layer-component (:bottom-layer puzzle))]
    [:div.puzzle
     [:span.layer.top top-canvas]
     [:span.layer.bottom bottom-canvas]
     (when debug?
       [:div
        "Top:" (p/pieces-str (:top-layer puzzle))
        ", "
        "Bottom: " (p/pieces-str (:bottom-layer puzzle))])]))
