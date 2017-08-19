(ns squanmate.ui.initial-rotation-adjuster
  (:require [squanmate.ui.common :as common]
            [reagent.core :as reagent]
            [squanmate.rotation :as rotation]
            [cats.monad.either :as either]
            [squanmate.slicing :as slicing]
            [cats.core :as m]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as puzzle]
            [squanmate.alg.manipulation :as manipulation]))

(defn possible-sliceable-rotations [layer]
  (let [possible-rotations (rotation/possible-rotations layer)
        sliceable-rotations (filter (fn [[result amount]]
                                      (slicing/layer-sliceable? (m/extract result)))
                                    possible-rotations)
        positive (for [[result amount] sliceable-rotations
                       :when (pos? amount)]
                   amount)
        negative (for [[result amount] sliceable-rotations
                       :when (neg? amount)]
                   amount)]
    ;; shorter rotations first in each case
    {:positive (sort < positive)
     :negative (sort > negative)}))

(defn next-sliceable-rotation [layer direction]
  (let [amounts (possible-sliceable-rotations layer)
        amount (condp = direction
                 :+ (first (:positive amounts))
                 :- (first (:negative amounts)))]
    (if (= puzzle/TopLayer (type layer))
      (types/->Rotations amount 0)
      (types/->Rotations 0 amount))))

(defn- usage-tip-component []
  [common/overlay-trigger
   {:overlay (reagent/as-element
              [common/popover {:id "initial-rotation-adjuster-tip"}
               "If you enter your algorithm first, changing the initial rotation
               will automatically adapt the algorithm so it will always work."])
    :placement :left}
   [common/label "Tip! :)"]])

(defn- rotate-layer [layer
                     initial-rotation-atom
                     algorithm-atom
                     direction]
  (let [rotations (next-sliceable-rotation layer direction)]
    (swap! initial-rotation-atom
           (fn [initial-rotation-string]
             (manipulation/try-update-alg-string
              initial-rotation-string
              (partial manipulation/prepend-initial-rotation rotations)))
           manipulation/combine-rotations rotations)
    (print "rotating this layer by " rotations)))

(defn- rotation-controls [{:keys [top-layer bottom-layer] :as puzzle}
                          initial-rotation-atom
                          algorithm-atom]
  [:div
   [:div "Top: "]
   [common/button {:on-click #(rotate-layer top-layer
                                            initial-rotation-atom
                                            algorithm-atom
                                            :-)}
    [common/glyphicon {:glyph :minus}]]
   [common/button {:on-click #(rotate-layer top-layer
                                            initial-rotation-atom
                                            algorithm-atom
                                            :+)}
    [common/glyphicon {:glyph :plus}]]

   [:div "Bottom: "]
   [common/button {:on-click #()}
    [common/glyphicon {:glyph :minus}]]
   [common/button
    [common/glyphicon {:glyph :plus}]]])

(defn rotation-adjuster [puzzle
                         initial-rotation-atom
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
      [rotation-controls
       puzzle
       initial-rotation-atom
       algorithm-atom]]]]])
