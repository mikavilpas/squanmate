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
    (with-temporary-rotation (* position 30)
      #(do
         (q/no-fill)
         (apply q/triangle (vals coords))))
    coords))

(def magic-numbers "( ͡° ͜ʖ ͡°)"
  (memoize (fn [size]
             {:a (* size (/ 110 400))
              :b (* size (/ -55 400))
              :c (* size (/ 205 400))})))

(defn- draw-corner-at [position {:keys [size bot edge-width]
                                 :as data}]
  (with-temporary-rotation (* position 30)
    #(let [{:keys [a b c] :as magic} (magic-numbers size)]
       (q/no-stroke)

       ;; these triangles should be used to set the fill color. not currently
       ;; used, but planned in the future
       (q/fill 150 205 105 200)
       (q/triangle 0 0
                   (- a) a
                   edge-width bot)
       (q/triangle (- a) a
                   b c
                   edge-width bot)
       (q/stroke 0)
       ;; stroke the edges of the piece so it looks the same as edges

       (q/line 0 0 (- a) a)
       (q/line (- a) a b c)
       (q/line b c edge-width bot)
       (q/line edge-width bot 0 0))))

(defn- draw-top-layer [state]
  (let [size (:size state)
        center (/ size 2)
        data {:edge-width (/ size 10)
              :bot (* size 0.375)
              :size size}]
    (q/stroke 0)
    (q/background 255)
    ;; to see the canvas edges when developing
    (q/fill 255)
    (q/rect 0 0 (- size 1) (- size 1))

    ;; start drawing from the center
    (q/translate center center)

    (draw-corner-at 1 data)
    (draw-edge-at 3 data)
    (draw-corner-at 4 data)
    (draw-edge-at 6 data)
    (draw-corner-at 7 data)
    (draw-edge-at 9 data)
    (draw-corner-at 10 data)
    (draw-edge-at 12 data))

  ;; This needs to be the last statement. After it no changes will be visible.
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
