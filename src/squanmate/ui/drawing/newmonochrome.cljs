(ns squanmate.ui.drawing.newmonochrome
  (:require [quil.middleware :as m]
            [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.pieces :as pieces]
            [squanmate.ui.drawing.util.quil-reagent :as quil-reagent]
            [squanmate.ui.drawing.color-settings :as color-settings]))

(defn- make-color-settings [{:keys [monochrome?
                                    use-back-as-front
                                    swap-top-and-bottom]
                             :or {monochrome? true}}]
  (cond-> color-settings/defaults
    monochrome?
    color-settings/make-monochrome

    use-back-as-front
    color-settings/swap-front-and-back

    swap-top-and-bottom
    color-settings/swap-top-and-bottom))

(defn layer-component [initial-layer settings]
  (let [my-state (reagent/atom {:layer initial-layer
                                :color-settings nil})]
    (fn render [layer settings]
      ;; It's a bit unfortunate but I can't get quil to see a change in the
      ;; given layer without a local current-layer state
      (swap! my-state assoc :layer layer)
      (let [shape-name (shapes/layer-shape-name (-> @my-state :layer))
            color-settings (make-color-settings settings)
            size (or (:size settings) 100)]
        (println "made color-settings " color-settings)
        [common/overlay-trigger
         {:overlay (reagent/as-element [common/tooltip {:id "test"}
                                        shape-name])
          :placement "top"}
         [:div {:style {"display" "inline-block"}}
          [quil-reagent/sketch
           :setup (pieces/setup-fn (-> @my-state :layer) size)
           :draw (fn [state]
                   (pieces/draw-layer state (:color-settings @my-state)))
           :update (fn [old-state]
                     (assoc-in old-state [:layer] (-> @my-state :layer)))
           :middleware [m/fun-mode]
           :size [size size]]]]))))

(defn monochrome-puzzle
  ([puzzle]
   [monochrome-puzzle puzzle {}])

  ([puzzle {:keys [debug? size monochrome?]
            :as settings}]
   [:div.puzzle {:style {:white-space :nowrap}}
    [:span.layer.top [layer-component (:top-layer puzzle) settings]]
    [:span.layer.bottom [layer-component (:bottom-layer puzzle) settings]]
    (when debug?
      [:div
       "Top:" (p/pieces-str (:top-layer puzzle))
       ", "
       "Bottom: " (p/pieces-str (:bottom-layer puzzle))])]))
