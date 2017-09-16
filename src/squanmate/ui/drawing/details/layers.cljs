(ns squanmate.ui.drawing.details.layers
  (:require [quil.core :as q]
            [squanmate.puzzle :as puzzle]
            [squanmate.slicing :as slicing]
            [squanmate.ui.drawing.details.pieces :as pieces]
            [squanmate.ui.drawing.details.count-positions :as count-positions]
            [squanmate.shapes :as shapes]))

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

(defn draw-layer [state settings]
  (let [size (:size state)
        center (/ size 2)
        layer (:layer state)
        data (merge settings {:edge-width (/ size 10)
                              :bot (* size 0.375)
                              :size size})]
    (q/background 255)

    ;; start drawing from the center
    (q/translate center center)
    (q/scale 0.87)

    (pieces/draw-slice-point data)

    (condp = (type layer)
      puzzle/TopLayer (draw-top-layer layer data)
      puzzle/BottomLayer (draw-bottom-layer layer data)
      ;; for tests
      shapes/Shape (draw-top-layer layer data))

    (when-let [count-positions (:count-positions settings)]
      (count-positions/draw-position-group-a (first count-positions) data)
      (count-positions/draw-position-group-b (second count-positions) data))))
