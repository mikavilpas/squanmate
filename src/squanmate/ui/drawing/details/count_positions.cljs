(ns squanmate.ui.drawing.details.count-positions
  (:require [squanmate.ui.drawing.details.utils :refer [with-temporary-rotation]]
            [quil.core :as q]))

(defn- draw-positions [positions color]
  (doseq [position positions]
    ;; a rotation of 1 goes clockwise on the top layer. Its purpose is to show
    ;; the rotation required to reach that position.
    (with-temporary-rotation (- 60 (* position 30))
      #(do
         (apply q/fill color)
         ;; todo scaling
         (q/ellipse 40 40 17 17)))))

(def ^:private point1-color [100 220 220])
(def ^:private point2-color [220 100 120])

(defn draw-position-group-a [positions]
  (draw-positions positions point1-color))

(defn draw-position-group-b [positions]
  (draw-positions positions point2-color))
