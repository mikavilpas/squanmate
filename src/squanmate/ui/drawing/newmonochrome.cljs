(ns squanmate.ui.drawing.newmonochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [clojure.string :as string]
            [squanmate.shapes :as shapes]
            [cljsjs.react-bootstrap]
            [squanmate.ui.drawing.util.quil-reagent :as quil-reagent]
            [quil.core :as q]
            [quil.middleware :as m]
            [squanmate.ui.common :as common]))

(defrecord DrawLayerState [layer size])

;; todo use q/with-rotation macro
(defn- with-temporary-rotation [degrees function]
  (q/rotate (q/radians degrees))
  (function)
  (q/rotate (q/radians (- degrees))))

(defn- edge-coordinates [{:keys [bot edge-width]}]
  (let [coords {:x1 0 :y1 0
                :x2 (- edge-width) :y2 bot
                :x3 edge-width :y3 bot}]
    coords))

(defn- draw-edge-at [position data]
  (let [coords (edge-coordinates data)]
    (with-temporary-rotation (* (+ 1 position) 30)
      #(do
         ;; (q/no-fill)
         (apply q/triangle (vals coords))))
    coords))

(def magic-numbers "( ͡° ͜ʖ ͡°)"
  (memoize (fn [size]
             {:a (* size (/ 110 400))
              :b (* size (/ -55 400))
              :c (* size (/ 205 400))})))

(def ^:private monochrome-color 169)

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

(defn- draw-top-layer [state]
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

    (doseq [[piece position] (slicing/pieces-and-their-positions layer)]
      (condp = (:type piece)
        "c"
        (draw-corner-at position data)

        "e"
        (draw-edge-at position data)

        (println (new js/Error (str "warning: cannot draw unknown top layer piece " piece))))))

  ;; This needs to be the last statement. After it no animation changes will be visible.
  ;; Comment it to have a delicious developer hot load experience
  #_(q/no-loop))

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
        (throw (new js/Error "drawing the bottom layer is not implemented yet"))))

(defn layer-component [layer & {:keys [size]
                                :or {size 400}}]
  (let [state (DrawLayerState. layer size)
        draw-function-var (draw layer)]
    [quil-reagent/sketch
     :setup (fn []
              ;; there is no need for animation at the moment. just a static image
              ;; will do perfectly fine.
              (q/frame-rate 1)
              (q/smooth)
              state)
     :draw draw-function-var
     ;; no changes to state are needed / allowed
     :update (constantly state)
     :middleware [m/fun-mode]
     :size [size size]]))

(defn monochrome-puzzle [puzzle & debug?]
  (let [top-canvas (layer-component (:top-layer puzzle))]
    ;; bottom-canvas (layer-component (:bottom-layer puzzle))
    [:div.puzzle
     [:span.layer.top top-canvas]
     #_[:span.layer.bottom bottom-canvas]
     (when debug?
       [:div
        "Top:" (p/pieces-str (:top-layer puzzle))
        ", "
        "Bottom: " (p/pieces-str (:bottom-layer puzzle))])]))