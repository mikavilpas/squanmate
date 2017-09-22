(ns squanmate.ui.scramble
  (:require [reagent.core :as reagent]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [clojure.string :as str]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.ui.rotation-adjuster-controls :as rac]))

(defn default-state []
  (reagent/atom {:imported? false
                 :scramble {:scramble-algorithm nil
                            :rotations nil}}))

(defn- invalid-scramble [scramble-alg error]
  [:div "Invalid scramble: " (pr-str error)])

(def final-rotation-adjustment-for-scramble-visualization
  (reify rac/LayerRotationAdjustmentStrategy
    (rotate-layer [this
                   layer
                   rotation-atom
                   algorithm-atom
                   rotations]
      (letfn [(append-rotation [alg-string]
                (manipulation/try-update-alg-string
                 alg-string
                 (partial manipulation/append-final-rotation rotations)))]
        (swap! rotation-atom append-rotation)
        (swap! algorithm-atom append-rotation)))))

(defn- show-successful-scramble [puzzle state]
  [:div.center.vertical
   [newmonochrome/monochrome-puzzle puzzle {:monochrome? false
                                            :size 200}]
   [shape-scrambler/scramble-preview (-> @state :scramble :scramble-algorithm)]
   [rac/rotation-controls
    puzzle
    (reagent/cursor state [:scramble :rotations])
    (reagent/cursor state [:scramble :scramble-algorithm])
    final-rotation-adjustment-for-scramble-visualization]])

(defn- puzzle-view [state]
  (let [scramble-alg (-> @state :scramble :scramble-algorithm)
        ;; todo apply final rotation to the alg
        puzzle-either (execution/transformation-result puzzle/square-square
                                                       scramble-alg)]
    (either/branch puzzle-either
                   (fn [error]
                     [invalid-scramble scramble-alg error])
                   (fn [transformation-result]
                     [show-successful-scramble (:puzzle transformation-result) state]))))

(defn- mark-alg-imported [state]
  (swap! state assoc :imported? true))

(defn- enter-alg-view [state]
  (let [given-alg (reagent/cursor state [:scramble :scramble-algorithm])]
    [:div
     [common/input-box given-alg "Enter your scramble"]
     (when-not (str/blank? @given-alg)
       [:div.center.top17
        [common/button {:bs-size "large"
                        :bs-style "success"
                        :on-click #(mark-alg-imported state)}
         "Import"]])]))

(defn component [state]
  [:div
   (if (:imported? @state)
     [puzzle-view state]
     [enter-alg-view state])])
