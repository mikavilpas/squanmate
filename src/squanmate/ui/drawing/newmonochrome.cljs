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

(defn layer-component [initial-layer & {:keys [size]
                                        :or {size 100}}]
  (let [draw-function-var (draw initial-layer)
        current-layer (reagent/atom initial-layer)]
    (fn render [layer & {:keys [size]
                        :or {size 100}}]
      (reset! current-layer (assoc-in layer [:debug-name] (shapes/layer-shape-name @current-layer)))
      (let [shape-name (shapes/layer-shape-name @current-layer)]
        [common/overlay-trigger
         {:overlay (reagent/as-element [common/tooltip {:id "test"}
                                        shape-name])
          :placement "top"}
         [:div {:style { "display" "inline-block" }}
          shape-name
          [quil-reagent/sketch
           :setup (pieces/setup @current-layer size)
           :draw draw-function-var
           :update (fn [old-state]
                     (println "updating old state " (:debug-name (:layer old-state))
                              " to new state " (:debug-name (:layer @current-layer)))
                     (assoc-in old-state [:layer] @current-layer))
           :middleware [m/fun-mode]
           :size [size size]]]]))))

(defn monochrome-puzzle [puzzle & debug?]
  [:div.puzzle
   [:span.layer.top [layer-component (:top-layer puzzle)]]
   [:span.layer.bottom [layer-component (:bottom-layer puzzle)]]
   (when debug?
     [:div
      "Top:" (p/pieces-str (:top-layer puzzle))
      ", "
      "Bottom: " (p/pieces-str (:bottom-layer puzzle))])])
