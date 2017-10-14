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
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.pages.links :as links]))

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
     [case-selections state ep/all-cases]]
    #_[common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                                                " Permute last layer (PLL)"])
                   :event-key 2}
     [:div "todo"]]]])

(defn- settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Algorithm sets"])
                  :event-key 1}
    [alg-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 2}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn new-default-state []
  (reagent/atom {:selected-cases #{}
                 :draw-settings {:monochrome? false}}))

(defn- new-scramble-button [state]
  [:div
   (if (empty? (:selected-cases @state))
     [common/alert "Select some algs below to get started."]
     [common/button {:on-click #(scramble-generation/set-new-scramble! state)
                     :bs-style :success}
      "New scramble"])])

(defn- inspect-scramble-button [state]
  (when (:chosen-case-name @state)
    [common/button {:on-click #(links/set-link-to-scramble (:scramble-algorithm @state))}
     [common/glyphicon {:glyph :search}]
     " Inspect"]))

(defn- repeat-scramble-button [state]
  (when (:chosen-case-name @state)
    [common/button {:on-click #(scramble-generation/new-scramble state (:chosen-case-name @state))}
     [:span [common/glyphicon {:glyph :repeat}]]
     " Repeat case"]))

(defn- action-buttons [state]
  [:div.center
   [repeat-scramble-button state]
   [new-scramble-button state]
   [inspect-scramble-button state]])

(defn trainer-component [state]
  [:div
   [:div.center.vertical [action-buttons state]]
   [:div.center.top17 [puzzle-preview state]]
   [:div.center.top17 [scramble-preview (:scramble-algorithm @state)]]
   [settings state]])
