(ns squanmate.ui.drawing.pieces
  (:require [quil.core :as q]
            [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.slicing :as slicing]))

(def ^:private monochrome-color 169)

(defrecord DrawLayerState [layer size])

(defn setup [layer size]
  (fn []
    ;; there is no need for animation at the moment. just a static image
    ;; will do perfectly fine.
    (q/frame-rate 1)
    (q/smooth)
    (q/stroke 0)
    (q/background 255)
    (q/fill monochrome-color)
    (DrawLayerState. layer size)))

;; todo use q/with-rotation macro
(defn- with-temporary-rotation [degrees function]
  (q/rotate (q/radians degrees))
  (function)
  (q/rotate (q/radians (- degrees))))

(defn- draw-edge-at [position {:keys [bot edge-width]}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(do
       ;; (q/no-fill)
       (q/triangle 0 0
                   (- edge-width) bot
                   edge-width bot))))

(def ^:private magic-numbers "( ͡° ͜ʖ ͡°)"
  (memoize (fn [size]
             ;; these are the relative positions of a corner piece's points. I
             ;; used a test canvas of size 400 to get these with brute
             ;; force (I'm not the best at trigonometry), so that's why you see
             ;; a (/ foo 400)
             {:a (* size (/ 110 400))
              :b (* size (/ -55 400))
              :c (* size (/ 205 400))})))

(defn- draw-corner-at [position {:keys [size bot edge-width]
                                 :as data}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(let [{:keys [a b c] :as magic} (magic-numbers size)]

       ;; drawing triangles without a store color makes them have a 1px wide
       ;; empty stroke that appears as white (the background color). Work around
       ;; this by using the fill color as the stroke color
       (q/stroke monochrome-color)
       ;; these triangles should be used to set the fill color. not currently
       ;; used, but planned in the future
       ;; (q/fill 150 205 105 200)
       (q/triangle 0 0
                   (- a) a
                   edge-width bot)
       (q/triangle (- a) a
                   b c
                   edge-width bot)
       (q/line (- a) a edge-width bot)
       (q/stroke 0)
       ;; stroke the edges of the piece so it looks the same as edges

       (q/line 0 0 (- a) a)
       (q/line (- a) a b c)
       (q/line b c edge-width bot)
       (q/line edge-width bot 0 0))))

(defn- draw-layer-pieces [state]
  (let [size (:size state)
        center (/ size 2)
        layer (:layer state)
        data {:edge-width (/ size 10)
              :bot (* size 0.375)
              :size size}]
    (q/stroke 0)
    (q/background 255)
    (q/fill monochrome-color)

    ;; start drawing from the center
    (q/translate center center)
    (q/scale 0.95)

    (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
      (condp = (:type piece)
        "c"
        (draw-corner-at position data)

        "e"
        (draw-edge-at position data)

        (println (new js/Error (str "warning: cannot draw unknown piece " piece)))))))

(defn draw-top-layer [state]
  (draw-layer-pieces state))

(defn draw-bottom-layer [state]
  (draw-layer-pieces state))
