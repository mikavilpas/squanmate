(ns squanmate.ui.drawing.pieces
  (:require [quil.core :as q]
            [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.slicing :as slicing]))

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

;; todo use q/with-rotation macro
(defn- with-temporary-rotation [degrees function]
  (q/rotate (q/radians degrees))
  (function)
  (q/rotate (q/radians (- degrees))))

(defn- piece-stroke []
  (q/stroke-weight 1)
  (q/stroke 0))

(defn- get-color [draw-settings piece side]
  (let [piece-side (-> piece :colors side)]
    (get (:colors draw-settings) piece-side)))

(defn- draw-edge-at [piece
                     position
                     {:keys [bot edge-width]
                      :as data}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(do
       (piece-stroke)
       (apply q/fill (get-color data piece :top))
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

(defn- draw-slice-point [size]
  (with-temporary-rotation -75
    #(let [{:keys [c]} (magic-numbers size)]
       (q/stroke-weight 2)
       (q/stroke 200)
       (q/line (- c) 0 c 0))))

(defn- draw-corner-at [piece
                       position
                       {:keys [size
                               bot
                               edge-width
                               monochrome?]
                        :as data}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(let [{:keys [a b c] :as magic} (magic-numbers size)]

       ;; drawing triangles without a store color makes them have a 1px wide
       ;; empty stroke that appears as white (the background color). Work around
       ;; this by using the fill color as the stroke color
       (piece-stroke)
       (apply q/stroke (get-color data piece :top))
       (apply q/fill (get-color data piece :top))
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
       (piece-stroke)

       ;; stroke the edges of the piece so it looks the same as edges
       (q/line 0 0 (- a) a)
       (q/line (- a) a b c)
       (q/line b c edge-width bot)
       (q/line edge-width bot 0 0))))

(defn draw-settings [settings]
  (let [top-color (if (:monochrome? settings)
                    [169]
                    [255])]
    ;; overwrite default settings with given ones
    (merge {:colors {:top top-color
                     :bottom [0 200 200]

                     :front [150 200 240]
                     :left [140 30 200]
                     :back [25 70 80]
                     :right [180 100 50]}}
           settings)))

(defn draw-layer [state draw-settings]
  (let [size (:size state)
        center (/ size 2)
        layer (:layer state)
        data (merge draw-settings
                    {:edge-width (/ size 10)
                     :bot (* size 0.375)
                     :size size})]
    (piece-stroke)
    (q/background 255)

    ;; start drawing from the center
    (q/translate center center)
    (q/scale 0.95)

    (draw-slice-point size)

    (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
      (condp = (:type piece)
        "c"
        (draw-corner-at piece position data)

        "e"
        (draw-edge-at piece position data)

        (println (new js/Error (str "warning: cannot draw unknown piece " piece)))))))
