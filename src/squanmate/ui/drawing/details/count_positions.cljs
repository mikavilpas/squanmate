(ns squanmate.ui.drawing.details.count-positions
  (:require [squanmate.ui.drawing.details.utils :refer [with-temporary-rotation]]
            [quil.core :as q]))

(defn- draw-positions [positions color {:keys [size]}]
  (letfn [(scale [n]
            (* size (/ n 200)))]
    (doseq [position positions]
      ;; a rotation of 1 goes clockwise on the top layer. Its purpose is to show
      ;; the rotation required to reach that position.
      (with-temporary-rotation (- 60 (* position 30))
        #(do
           (apply q/fill color)
           (q/ellipse (scale 40) (scale 40)
                      (scale 17) (scale 17)))))))

(def ^:private point1-color [100 220 220])
(def ^:private point2-color [250 60 130])

(defn draw-position-group-a [positions data]
  (draw-positions positions point1-color data))

(defn draw-position-group-b [positions data]
  (draw-positions positions point2-color data))
