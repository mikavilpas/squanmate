(ns squanmate.scramblers.shape-scrambler
  (:require [reagent.core :as reagent]
            [squanmate.pages.links :as links]
            [squanmate.scramblers.shape-scrambler.actions :as a]
            [squanmate.ui.color-chooser :as color-chooser]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.layer-selector :as layer-selector]
            [squanmate.ui.middle-layer-controls :as middle-layer-controls]
            [squanmate.scramblers.shape-scrambler.default-scrambler :as default-scrambler]
            [squanmate.ui.case-counter :as case-counter]))

(defn selected-shapes-counter [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)]
    [case-counter/selected-cases-counter layer-count 90]))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn- all-shapes-selection-buttons [state]
  [:div
   [common/button {:on-click #(a/select-all-shapes state)} "Select all"]
   " "
   [common/button {:on-click #(a/select-no-shapes state)} "Select none"]])

(defn- shape-selection-settings [state]
  [:div
   [selected-shapes-counter state]
   [all-shapes-selection-buttons state]
   [:div.top30
    "Select available shapes for scrambles by filtering:"]
   [layer-selector/layer-selector state]])

(defn settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Cases"])
                  :event-key 1}
    [shape-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :wrench}]
                                               " Scramble options"])
                  :event-key 2}
    [middle-layer-controls/controls (reagent/cursor state [:middle-layer-settings])]]])

;; let this module own its state schema by having it defined inside this file
(defn new-state []
  ;; Allowed shapes are stored as sets of their layers. This makes adding and
  ;; removing them very easy in code.
  (let [state (reagent/atom
               {:puzzle nil
                :chosen-shapes nil
                :selected-shapes #{(set ["square" "square"])}
                :scramble-algorithm nil

                ;; :draw-settings are needed so this can work without using
                ;; global colors (in testing), but can be overridden with global
                ;; colors
                :draw-settings newmonochrome/default-settings
                :middle-layer-settings (deref (middle-layer-controls/default-state))})]
    state))

(defn- repeat-case-button [state]
  [common/split-button {:on-click #(a/set-new-repeat-scramble state)
                        :id "repeat-case"
                        :title (reagent/as-element [:span [common/glyphicon {:glyph :repeat}]
                                                    " Repeat case"])}
   [common/menu-item {:on-select #(a/set-new-scramble-with-parity state :same-relative-parity)}
    "Repeat with same parity"]
   [common/menu-item {:on-select #(a/set-new-scramble-with-parity state :opposite-relative-parity)}
    "Repeat with opposite parity"]])

(defn- new-scramble-button [state]
  [common/split-button {:on-click #(a/set-new-random-scramble state)
                        :id "new-scramble"
                        :title "New scramble"
                        :disabled (a/no-cases-selected? state)
                        :bs-style :success}
   [common/menu-item {:on-click #(a/deselect-case-and-generate-new-scramble! state)}
    "Deselect this case and generate a new scramble"]])

(defn- inspect-scramble-button [state]
  [common/button {:on-click #(links/set-link-to-scramble (:scramble-algorithm @state))}
   [common/glyphicon {:glyph :search}]
   " Inspect"])

(defn- action-buttons [state]
  [:div.center
   [repeat-case-button state]
   [new-scramble-button state]
   [inspect-scramble-button state]])

(defn- puzzle-preview [state draw-settings]
  (if-let [p (:puzzle @state)]
    [newmonochrome/monochrome-puzzle p draw-settings]
    [common/alert "Select some cases and click 'New scramble' to get started."]))

(defn scramble-component [state]
  (let [draw-settings (assoc (:draw-settings @state)
                             :size 180)]
    [:div
     [:div.bottom17
      [action-buttons state]]
     [:div.center
      [puzzle-preview state draw-settings]]
     [:div.center
      [scramble-preview (:scramble-algorithm @state)]]
     [settings state]]))
