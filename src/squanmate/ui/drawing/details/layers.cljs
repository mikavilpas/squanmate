(ns squanmate.ui.drawing.details.layers
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.slicing :as slicing]
            [squanmate.puzzle :as puzzle]
            [squanmate.ui.drawing.color-settings :as color-settings]
            [squanmate.ui.drawing.details.pieces :as pieces]))

(defrecord DrawLayerState [layer size])

(defn setup-fn [layer size]
  (fn []
    ;; there is no need for animation at the moment. just a static image
    ;; will do perfectly fine.
    (q/frame-rate 10)
    (q/smooth)
    (q/stroke 0)
    (q/background 255)
    (DrawLayerState. layer size)))

(defn- draw-top-layer [layer data]
  (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
    (condp = (:type piece)
      "c"
      (pieces/draw-corner-at piece position data)

      "e"
      (pieces/draw-edge-at piece position data)

      (println (new js/Error (str "warning: cannot draw unknown piece " piece))))))

(defn- draw-bottom-layer [layer data]
  ;; drawing the bottom layer is like drawing the top, but its pieces start at a
  ;; different position. This needs to be accounted for.
  (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
    (condp = (:type piece)
      "c"
      (pieces/draw-corner-at piece (+ 6 position) data)

      "e"
      (pieces/draw-edge-at piece (+ 6 position) data)

      (println (new js/Error (str "warning: cannot draw unknown piece " piece))))))

(defn draw-layer [state draw-settings]
  (let [size (:size state)
        center (/ size 2)
        layer (:layer state)
        data {:edge-width (/ size 10)
              :bot (* size 0.375)
              :size size
              :draw-settings draw-settings}]
    (q/background 255)

    ;; start drawing from the center
    (q/translate center center)
    (q/scale 0.87)

    (pieces/draw-slice-point data)

    (condp = (type layer)
      puzzle/TopLayer (draw-top-layer layer data)
      puzzle/BottomLayer (draw-bottom-layer layer data))))
