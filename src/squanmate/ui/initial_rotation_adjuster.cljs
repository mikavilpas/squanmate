(ns squanmate.ui.initial-rotation-adjuster
  (:require [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

(defn- usage-tip-component []
  [common/overlay-trigger
   {:overlay (reagent/as-element
              [common/popover {:id "initial-rotation-adjuster-tip"}
               "If you enter your algorithm first, changing the initial
                   rotation will automatically adapt the algorithm so it will
                   always work."])
    :placement :left}
   [common/label "Tip"]])

(defn- rotation-controls []
  [:div
   [common/button
    [common/glyphicon {:glyph :minus}]]
   [common/button
    [common/glyphicon {:glyph :plus}]]])

(defn rotation-adjuster [initial-rotation-atom
                         algorithm-atom]
  [common/accordion
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :repeat}]
                                               " Initial rotation"])}
    [:div
     "To get a more comfortable count position for doing your parity count,
     adjust the layers' starting position as you like."
     [:div.top5
      [usage-tip-component]]
     [:div.center
      [:div.top3
       "Top: " [rotation-controls]
       "Bottom: " [rotation-controls]]]]]])
