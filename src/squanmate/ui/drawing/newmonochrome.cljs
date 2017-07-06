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

(defn- draw-top-layer [layer]
  (print "drawing top layer")

  (q/background 0)
  (q/background 255)

  ;; This needs to be the last statement. After it no changes will be visible.
  ;; Uncomment it to have a delicious developer hot load experience
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
                                :or {size 100}}]
  [quil-reagent/sketch
   :setup (fn []
            ;; there is no need for animation at the moment. just a static image
            ;; will do perfectly fine.
            (q/frame-rate 1)
            layer)
   :draw (draw layer)
   ;; no changes to state are needed / allowed
   :update (constantly layer)
   :middleware [m/fun-mode]
   :size [size size]])

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
