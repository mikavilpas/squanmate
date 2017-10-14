(ns squanmate.scramblers.alg-trainer
  (:require [cats.core :as m]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as p]
            [squanmate.scramblers.alg-trainer.case-selection :as selection]
            [squanmate.scramblers.alg-trainer.scramble-generation :as scramble-generation]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.ui.color-chooser :as color-chooser]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]))

(defn- puzzle-for-alg [alg]
  (->> alg
       (execution/transformation-result p/square-square)
       m/extract
       :puzzle))

(defn- puzzle-preview [state]
  (when-let [puzzle (:puzzle @state)]
    (let [draw-settings (assoc (:draw-settings @state)
                               :size 180)]
      [newmonochrome/monochrome-puzzle puzzle draw-settings])))

(defn- scramble-preview [s]
  (when (not (str/blank? s))
    [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]]))

(defn- new-scramble-button [state]
  [:div
   (if (empty? (:selected-cases @state))
     [common/alert "Select some algs below to get started."]
     [common/button {:on-click #(scramble-generation/new-scramble state)}
      "New scramble"])])

(defn- case-selection-component [state case]
  (let [[case-name alg] case
        selected? (selection/case-selected? state case-name)]
    [:div.col-xs-4
     [common/checkbox {:inline true
                       :checked selected?
                       :on-change #(selection/select-or-deselect! state case-name)}
      case-name]]))

(defn- case-selections [state cases]
  [:div
   [:div.container-fluid
    (for [[case-name alg] cases]
      ^{:key case-name}
      [case-selection-component state [case-name alg]])]
   [:div.center
    [common/button {:on-click #(selection/select-cases! state cases)} "Select all"]
    [common/button {:on-click #(selection/deselect-cases! state cases)} "Select none"]]])

(defn- alg-selection-settings [state]
  [:div
   [common/accordion {:default-active-key 1}
    [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                                                " Edge permutation (EP)"])
                   :event-key 1}
     [:div
      [:div "With no parity"
       [case-selections state ep/cases-with-no-parity]]

      [:div "With parity"
       [case-selections state ep/cases-with-parity]]]]
    [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                                                " Permute last layer (PLL)"])
                   :event-key 2}
     [:div "todo"]]]])

(defn- settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Settings"])
                  :event-key 1}
    [alg-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 2}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn new-default-state []
  (reagent/atom {:selected-cases #{}
                 :draw-settings {:monochrome? false}}))

(defn trainer-component [state]
  [:div
   [:div.center.vertical
    [new-scramble-button state]
    [puzzle-preview state]]
   [:div.center.top17
    [scramble-preview (:scramble-algorithm @state)]]
   [:div.center
    [settings state]]])
