(ns squanmate.ui.initial-rotation-adjuster
  (:require [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as puzzle]
            [squanmate.alg.rotation :as rotation]
            [squanmate.slicing :as slicing]
            [squanmate.ui.common :as common]
            [squanmate.ui.rotation-adjuster-controls :as rac]))

(defn- usage-tip-component []
  [common/overlay-trigger
   {:overlay (reagent/as-element
              [common/popover {:id "rotation-adjuster-tip"}
               "If you enter your algorithm first, changing the rotation
               will automatically adapt the algorithm so it will always work."])
    :placement :left}
   [common/label "Tip! :)"]])

(def initial-rotation-adjustment-for-parity-count
  (reify rac/LayerRotationAdjustmentStrategy
    (rotate-layer [this
                   layer
                   rotation-atom
                   algorithm-atom
                   rotations]
      (swap! rotation-atom
             (fn [rotation-string]
               (manipulation/try-update-alg-string
                rotation-string
                (partial manipulation/prepend-initial-rotation rotations))))
      (swap! algorithm-atom
             (fn [algorithm-string]
               (manipulation/try-update-alg-string
                algorithm-string
                (partial manipulation/prepend-initial-rotation
                   (manipulation/negate-step rotations))))))))

;; todo move to its own file
(defn initial-rotation-adjuster [puzzle
                                 rotation-atom
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
       [rac/rotation-controls
        puzzle
        rotation-atom
        algorithm-atom
        initial-rotation-adjustment-for-parity-count]]]]])
