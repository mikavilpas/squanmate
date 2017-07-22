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

(def ^:private magic-numbers "( ͡° ͜ʖ ͡°)"
  (memoize (fn [{:keys [size edge-width bot] :as data}]
             ;; these are the relative positions of a corner piece's points. I
             ;; used a test canvas of size 400 to get these with brute
             ;; force (I'm not the best at trigonometry), so that's why you see
             ;; a (/ foo 400)
             (let [scale #(* size (/ % 400))
                   {:keys [a b c] :as m} {:a (scale 110)
                                          :b (scale -55)
                                          :c (scale 205)}]
               (merge m {:corner-color-b-edges [(- a) a
                                                (- (scale -10) a) (+ (scale 10) a)
                                                (- b (scale 5)) (+ (scale 19) c)
                                                b c]

                         :corner-color-a-edges [b c
                                                (- b (scale 5)) (+ (scale 19) c)
                                                (+ (scale 3) edge-width) (+ (scale 14) bot)
                                                edge-width bot]
                         :edge-color-edges [edge-width bot
                                            (+ 4 edge-width) (+ 14 bot)
                                            (- -4 edge-width) (+ 14 bot)
                                            (- edge-width) bot]})))))

(defn- draw-edge-at [piece
                     position
                     {:keys [bot edge-width monochrome?]
                      :as data}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(do
       (piece-stroke)
       (apply q/fill (get-color data piece :top))
       (q/triangle 0 0
                   (- edge-width) bot
                   edge-width bot)
       (when (not monochrome?)
         (q/stroke-weight 1)
         (apply q/stroke (get-color data piece :a))
         (apply q/fill (get-color data piece :a))
         (apply q/quad (:edge-color-edges (magic-numbers data)) )))))

(defn- draw-slice-point [data]
  (with-temporary-rotation -75
    #(let [{:keys [c]} (magic-numbers data)]
       (q/stroke-weight 2)
       (q/stroke 200)
       (q/line (- c) 0 c 0))))

(defn- draw-corner-colors [piece
                           {:keys [size bot edge-width monochrome?]
                            :as data}
                           {:keys [a b c] :as magic}]
  (q/stroke-weight 1)

  ;; first color
  (apply q/stroke (get-color data piece :a))
  (apply q/fill (get-color data piece :a))
  (apply q/quad (:corner-color-a-edges magic))

  ;; second color
  (apply q/stroke (get-color data piece :b))
  (apply q/fill (get-color data piece :b))
  (apply q/quad (:corner-color-b-edges magic)))

(defn- draw-corner-at [piece
                       position
                       {:keys [size bot edge-width monochrome?]
                        :as data}]
  (let [{:keys [a b c] :as magic} (magic-numbers data)
        rotation-amount (* (+ 1 position) 30)]

    ;; drawing triangles without a store color makes them have a 1px wide
    ;; empty stroke that appears as white (the background color). Work around
    ;; this by using the fill color as the stroke color
    (with-temporary-rotation rotation-amount
      #(do
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
         (q/line edge-width bot 0 0)

         (when (not monochrome?)
           (draw-corner-colors piece data magic))))))

(defn draw-settings [settings]
  (let [top-side-color (if (:monochrome? settings)
                    [169]
                    [253])]
    ;; overwrite default settings with given ones
    (merge {:colors {:top top-side-color
                     ;; http://paletton.com/#uid=7001c0knm++bq+PhV+Yt1+WH9ZC
                     :bottom [255, 254, 69] ;yellow

                     :front [255, 166, 69] ;orange
                     :left [99, 96, 255]   ;blue
                     :back [255, 69, 69]   ;red
                     :right [69, 245, 69]  ;green
                     }}
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
    (q/scale 0.87)

    (draw-slice-point data)

    (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
      (condp = (:type piece)
        "c"
        (draw-corner-at piece position data)

        "e"
        (draw-edge-at piece position data)

        (println (new js/Error (str "warning: cannot draw unknown piece " piece)))))))
