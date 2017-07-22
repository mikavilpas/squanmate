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

(defn layer-component [initial-layer & {:keys [size]
                                        :or {size 100}}]
  (let [current-layer (reagent/atom initial-layer)]
    (fn render [layer & {:keys [size]
                        :or {size 100}}]
      ;; It's a bit unfortunate but I can't get quil to see a change in the
      ;; given layer without a local current-layer state
      (reset! current-layer layer)
      (let [shape-name (shapes/layer-shape-name @current-layer)
            draw-settings (pieces/draw-settings {:monochrome? false})]
        [common/overlay-trigger
         {:overlay (reagent/as-element [common/tooltip {:id "test"}
                                        shape-name])
          :placement "top"}
         [:div {:style { "display" "inline-block" }}
          [quil-reagent/sketch
           :setup (pieces/setup-fn @current-layer size)
           :draw (fn [state]
                   (pieces/draw-layer state draw-settings))
           :update (fn [old-state]
                     (assoc-in old-state [:layer] @current-layer))
           :middleware [m/fun-mode]
           :size [size size]]]]))))

(defn monochrome-puzzle [puzzle & debug?]
  [:div.puzzle {:style {:white-space :nowrap}}
   [:span.layer.top [layer-component (:top-layer puzzle)]]
   [:span.layer.bottom [layer-component (:bottom-layer puzzle)]]
   (when debug?
     [:div
      "Top:" (p/pieces-str (:top-layer puzzle))
      ", "
      "Bottom: " (p/pieces-str (:bottom-layer puzzle))])])
