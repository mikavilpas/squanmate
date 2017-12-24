(ns squanmate.scramblers.alg-trainer
  (:require [cats.core :as m]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.alg.execution :as execution]
            [squanmate.pages.links :as links]
            [squanmate.puzzle :as p]
            [squanmate.scramblers.alg-trainer.case-selection :as selection]
            [squanmate.scramblers.alg-trainer.scramble-generation :as scramble-generation]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.scramblers.algsets.permute-last-layer :as pll]
            [squanmate.ui.color-chooser :as color-chooser]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.middle-layer-controls :as middle-layer-controls]
            [squanmate.services.storage :as storage]
            [squanmate.scramblers.algsets.algset :as algset]
            [squanmate.ui.case-counter :as case-counter]
            [clojure.set :as set]))

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
        selected? (selection/case-selected? state case)]
    [:div.col-xs-4
     [:div.selected-wrapper
      {:class (when selected? "selected-case")}
      [common/checkbox {:inline true
                        :checked selected?
                        :on-change #(selection/select-or-deselect! state [case-name alg])}
       case-name]]]))

(defn- case-group [cases state]
  [:div
   (for [[case-name alg] cases]
     ^{:key case-name}
     [case-selection-component state [case-name alg]])])

(defn- alg-group-selected-cases [cases state]
  (->> cases
       (into (hash-set))
       (set/intersection (:selected-cases @state))
       count))

(defn- alg-group-selection-counter [cases state]
  (let [selected-count (alg-group-selected-cases cases state)]
    (when (pos-int? selected-count)
      [case-counter/selected-cases-counter selected-count (count cases)])))

(defn- case-selections [state alg-set]
  [:div

   [:div.container-fluid [case-group (:even-cases alg-set) state]]
   [:div.center.top10
    [alg-group-selection-counter (:even-cases alg-set) state]]
   [:hr]
   [:div.container-fluid [case-group (:odd-cases alg-set) state]]
   [:div.center.top10
    [alg-group-selection-counter (:odd-cases alg-set) state]]

   [:hr]
   [:div.center.vertical
    [:div "Select:"]
    [:div
     [common/button
      {:on-click #(selection/select-cases! state (algset/all-cases alg-set))}
      "All"]
     [common/button
      {:on-click #(selection/deselect-cases! state (algset/all-cases alg-set))}
      "None"]
     [common/button
      {:on-click #(selection/select-cases! state (:even-cases alg-set))}
      "All even parity"]
     [common/button
      {:on-click #(selection/select-cases! state (:odd-cases alg-set))}
      "All odd parity"]]]])

(defn- algset-header [title]
  (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                       " " title]))

(defn- alg-selection-settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (algset-header "Edge permutation (EP)")
                  :event-key 1}
    [case-selections state ep/ep-algset]]
   [common/panel {:header (algset-header "Permute last layer (PLL)")
                  :event-key 2}
    [case-selections state pll/pll-algset]]])

(defn- settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Algorithm sets"])
                  :event-key 1}
    [alg-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :wrench}]
                                               " Scramble options"])
                  :event-key 2}
    [middle-layer-controls/controls (reagent/cursor state [:middle-layer-settings])]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 3}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn try-load-settings
  "If the user has previously saved settings, loads them and returns them (as a map)."
  []
  (when-let [state (storage/get-value "alg-trainer-settings")]
    state))

(defn save-settings! [state-map]
  (let [settings (->> (select-keys state-map [:selected-cases
                                              :draw-settings
                                              :middle-layer-settings])
                      (into {}))]
    (storage/save "alg-trainer-settings" settings)))

(defn new-default-state []
  (reagent/atom {:selected-cases #{}
                 :draw-settings {:monochrome? false}
                 :middle-layer-settings (deref (middle-layer-controls/default-state))}))

(defn- new-scramble-button [state]
  [:div
   (if (empty? (:selected-cases @state))
     [common/alert "Select some cases below to get started."]
     [common/button {:on-click #(scramble-generation/set-new-scramble! state)
                     :bs-style :success}
      "New scramble"])])

(defn- inspect-scramble-button [state]
  (when (:chosen-case @state)
    [common/button {:on-click #(links/set-link-to-scramble (:scramble-algorithm @state))}
     [common/glyphicon {:glyph :search}]
     " Inspect"]))

(defn- repeat-scramble-button [state]
  (when (:chosen-case @state)
    [common/button {:on-click #(scramble-generation/new-scramble state (:chosen-case @state))}
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
