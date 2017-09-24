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
            [squanmate.ui.rotation-adjuster-controls :as rac]
            [squanmate.ui.color-chooser :as color-chooser]
            [squanmate.pages.links :as links]
            [squanmate.ui.parity-analysis :as parity-analysis]))

(defn default-state []
  (let [draw-settings-state (deref (color-chooser/default-color-chooser-state))]
    (reagent/atom {:imported? false
                   :scramble {:scramble-algorithm nil
                              :rotations ""}
                   :draw-settings draw-settings-state})))

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

(defn- link-to-this-scramble [scramble-algorithm]
  [common/button {:on-click #(links/set-link-to-scramble scramble-algorithm)}
   "Link to this scramble"])

(defn- rotation-controls [puzzle state]
  [rac/rotation-controls
   puzzle
   (reagent/cursor state [:scramble :rotations])
   (reagent/cursor state [:scramble :scramble-algorithm])
   final-rotation-adjustment-for-scramble-visualization])

(defn- settings [puzzle state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " General"])
                  :event-key 1}
    [:div.center.vertical
     [rotation-controls puzzle state]
     [:div.top17
      [link-to-this-scramble (-> @state :scramble :scramble-algorithm)]]]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :pushpin}]
                                               " Parity"])
                  :event-key 2}
    [:div.center.space-around
     [parity-analysis/parity-analysis puzzle]
     [rotation-controls puzzle state]]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 3}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn- show-successful-scramble [puzzle state]
  (let [draw-settings (merge (:draw-settings @state)
                             {:size 200})]
    [:div
     [:div.center
      [newmonochrome/monochrome-puzzle puzzle draw-settings]]
     [:div.center
      [shape-scrambler/scramble-preview (-> @state :scramble :scramble-algorithm)]]
     [settings puzzle state]]))

(defn- clear-button [state]
  [common/button {:bs-size :large
                  :bs-style :danger
                  :on-click #(swap! state assoc :imported? false)} "Clear"])

(defn- puzzle-view [state]
  (let [scramble-alg (-> @state :scramble :scramble-algorithm)
        puzzle-either (execution/transformation-result puzzle/square-square
                                                       scramble-alg)]
    [:div
     [:div.center
      [clear-button state]]
     [:div.top17
      (either/branch puzzle-either
                     (fn [error]
                       [invalid-scramble scramble-alg error])
                     (fn [transformation-result]
                       [show-successful-scramble (:puzzle transformation-result) state]))]]))

(defn mark-alg-imported [state]
  (swap! state #(-> %
                    (assoc :imported? true)
                    (update-in [:scramble :scramble-algorithm] manipulation/format-alg))))

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
