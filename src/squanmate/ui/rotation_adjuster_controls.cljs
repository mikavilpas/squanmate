(ns squanmate.ui.rotation-adjuster-controls
  (:require [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.types :as types]
            [squanmate.puzzle :as puzzle]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]
            [squanmate.ui.common :as common]))

(defn possible-sliceable-rotations [layer]
  (let [possible-rotations (rotation/sliceable-rotations layer)
        positive (for [[result amount] possible-rotations
                       :when (pos? amount)]
                   amount)
        negative (for [[result amount] possible-rotations
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

(defprotocol LayerRotationAdjustmentStrategy
  (rotate-layer [this
                 layer
                 rotation-atom
                 algorithm-atom
                 direction]))


(defn- rotation-button [glyph handler]
  [common/button {:on-click handler}
   [common/glyphicon {:glyph glyph}]])

(defn layer-rotation-controls [layer
                               rotation-atom
                               algorithm-atom
                               rotation-strategy]
  (letfn [(rotate [direction]
            (let [rotations (next-sliceable-rotation layer direction)]
              (rotate-layer rotation-strategy
                            layer
                            rotation-atom
                            algorithm-atom
                            rotations)))]
    [:div
     [rotation-button :minus #(rotate :-)]
     [rotation-button :plus #(rotate :+)]]))

(defn rotation-controls [{:keys [top-layer bottom-layer] :as puzzle}
                         rotation-atom
                         algorithm-atom
                         rotation-strategy]
  [:div
   [:div "Top: "]
   [layer-rotation-controls top-layer rotation-atom algorithm-atom rotation-strategy]

   [:div "Bottom: "]
   [layer-rotation-controls bottom-layer rotation-atom algorithm-atom rotation-strategy]])
