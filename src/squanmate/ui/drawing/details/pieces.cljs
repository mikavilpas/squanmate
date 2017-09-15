(ns squanmate.ui.drawing.details.pieces
  (:require [quil.core :as q]
            [squanmate.ui.drawing.details.utils :refer [with-temporary-rotation]]))

(defn- piece-stroke []
  (q/stroke-weight 1)
  (q/stroke 0))

(defn- color-name->color [name-key]
  (let [colors {:gray [169]
                :white [253]
                :yellow [255, 254, 69]

                :orange [255, 166, 69]
                :blue [99, 96, 255]
                :red [255, 69, 69]
                :green [69, 245, 69]}
        color-value (get colors name-key)]
    color-value))

(defn- get-color [color-settings piece side]
  (let [piece-side (-> piece :colors side)
        color-name (get color-settings piece-side)
        color-value (color-name->color color-name)]
    color-value))

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
                                            (+ (scale 4) edge-width) (+ (scale 14) bot)
                                            (- (scale -4) edge-width) (+ (scale 14) bot)
                                            (- edge-width) bot]})))))

(defn- draw-edge-at [piece
                     position
                     {:keys [bot edge-width monochrome? color-settings]
                      :as data}]
  (with-temporary-rotation (* (+ 1 position) 30)
    #(do
       (piece-stroke)
       (apply q/fill (get-color color-settings piece :top))
       (q/triangle 0 0
                   (- edge-width) bot
                   edge-width bot)
       (when (not (:monochrome? color-settings))
         (q/stroke-weight 1)
         (let [edge-color (get-color color-settings piece :a)]
           (apply q/stroke edge-color)
           (apply q/fill edge-color)
           (apply q/quad (:edge-color-edges (magic-numbers data))))))))

(defn- draw-slice-point [data]
  (with-temporary-rotation -75
    #(let [{:keys [c]} (magic-numbers data)]
       (q/stroke-weight 2)
       (q/stroke 200)
       (q/line (- c) 0 c 0))))

(defn- draw-corner-colors [piece
                           {:keys [size bot edge-width monochrome? color-settings]
                            :as data}
                           {:keys [a b c] :as magic}]
  (q/stroke-weight 1)

  ;; first color-name->color
  (apply q/stroke (get-color color-settings piece :a))
  (apply q/fill (get-color color-settings piece :a))
  (apply q/quad (:corner-color-a-edges magic))

  ;; second color-name->color
  (apply q/stroke (get-color color-settings piece :b))
  (apply q/fill (get-color color-settings piece :b))
  (apply q/quad (:corner-color-b-edges magic)))

(defn- draw-corner-at [piece
                       position
                       {:keys [size bot edge-width color-settings]
                        :as data}]
  (let [{:keys [a b c] :as magic} (magic-numbers data)
        rotation-amount (* (+ 1 position) 30)]

    ;; drawing triangles without a store color-name->color makes them have a 1px wide
    ;; empty stroke that appears as white (the background color-name->color). Work around
    ;; this by using the fill color-name->color as the stroke color-name->color
    (with-temporary-rotation rotation-amount
      #(do
         (piece-stroke)
         (apply q/stroke (get-color color-settings piece :top))
         (apply q/fill (get-color color-settings piece :top))
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

         (when (not (:monochrome? color-settings))
           (draw-corner-colors piece data magic))))))
