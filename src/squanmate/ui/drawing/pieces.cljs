(ns squanmate.ui.drawing.pieces
  (:require [quil.core :as q]
            [quil.core :as q]
            [quil.middleware :as m]))

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
