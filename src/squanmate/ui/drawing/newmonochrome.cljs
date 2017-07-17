(ns squanmate.ui.drawing.newmonochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [squanmate.shapes :as shapes]
            [cljsjs.react-bootstrap]
            [squanmate.ui.drawing.util.quil-reagent :as quil-reagent]
            [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.pieces :as pieces]))

(defrecord DrawLayerState [layer size])

(defn- draw-layer-pieces [state]
  (let [size (:size state)
        center (/ size 2)
        layer (:layer state)
        data {:edge-width (/ size 10)
              :bot (* size 0.375)
              :size size}]
    (q/stroke 0)
    (q/background 255)
    (q/fill pieces/monochrome-color)

    ;; start drawing from the center
    (q/translate center center)
    (q/scale 0.95)

    (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
      (condp = (:type piece)
        "c"
        (pieces/draw-corner-at position data)

        "e"
        (pieces/draw-edge-at position data)

        (println (new js/Error (str "warning: cannot draw unknown piece " piece)))))))

(defn- draw-top-layer [state]
  (draw-layer-pieces state))

(defn- draw-bottom-layer [state]
  (draw-layer-pieces state))

(defprotocol Drawable
  (draw [layer]))

(extend-protocol Drawable
  shapes/Shape
  (draw [shape]
    #'draw-top-layer)

  p/TopLayer
  (draw [top-layer]
    #'draw-top-layer)

  p/BottomLayer
  (draw [bottom-layer]
    #'draw-bottom-layer))

(defn layer-component [layer & {:keys [size]
                                :or {size 100}}]
  (let [state (DrawLayerState. layer size)
        draw-function-var (draw layer)
        shape-name (shapes/layer-shape-name layer)]
    [common/overlay-trigger
     {:overlay (reagent/as-element [common/tooltip {:id "test"}
                                    shape-name])
      :placement "top"}
     [:div {:style { "display" "inline-block" }}
      [quil-reagent/sketch
       :setup (fn []
                ;; there is no need for animation at the moment. just a static image
                ;; will do perfectly fine.
                (q/frame-rate 1)
                (q/smooth)
                (q/stroke 0)
                (q/background 255)
                (q/fill pieces/monochrome-color)

                state)
       :draw draw-function-var
       ;; no changes to state are needed / allowed
       :update (constantly state)
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
