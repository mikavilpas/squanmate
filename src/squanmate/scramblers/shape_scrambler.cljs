(ns squanmate.scramblers.shape-scrambler
  (:require [reagent.core :as reagent]
            [squanmate.pages.links :as links]
            [squanmate.scramblers.shape-scrambler.actions :as a]
            [squanmate.ui.alg-display :as alg-display]
            [squanmate.ui.case-counter :as case-counter]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.inspection-timer :as timer]
            [squanmate.ui.inspection-timer-settings :as inspection-timer-settings]
            [squanmate.ui.layer-selector :as layer-selector]
            [squanmate.ui.middle-layer-controls :as middle-layer-controls]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.ui.shortcut-cheat-sheet :as shortcut-cheat-sheet]))

(defn selected-shapes-counter [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)]
    [case-counter/selected-cases-counter layer-count 90]))

(defn- timer []
  [timer/inspection-timer (timer/new-count-down-timer 15)])

(defn- scramble-preview [state]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble
   [common/well
    (when-let [s (:scramble-algorithm @state)]
      [:div.center.vertical

       [alg-display/rich-scramble-display s]

       (when (-> @state
                 :inspection-timer-settings
                 :show-inspection-timer?)
         [:div.top10 {:style {:width "100%"}}
          [timer state]])])]])

(defn- all-shapes-selection-buttons [state]
  [:div
   [common/button {:on-click #(a/select-all-shapes state)} "Select all"]
   " "
   [common/button {:on-click #(a/select-no-shapes state)} "Select none"]])

(defn- set-selected-single-case! [state chooser-state]
  (when-let [[top bottom] (shape-chooser/get-both-layers-or-nil @chooser-state)]
    (a/set-specific-shapes-scramble state [(hash-set top bottom)])))

(defn- select-single-case-component [state]
  (let [shown? (reagent/atom false)
        show! #(reset! shown? true)
        close! #(reset! shown? false)
        chooser-state (reagent/atom {:puzzle-chooser-layer-names nil})]
    (fn [state]
      [:div
       [common/modal {:show @shown?
                      :on-hide close!}
        [common/modal-header {:close-button true} "Select a case"]
        [common/modal-body
         [:div.container-fluid
          [shape-chooser/puzzle-chooser chooser-state]
          [:div.top10
           [common/button {:on-click #(do (set-selected-single-case! state chooser-state)
                                          (close!))
                           :bs-style "success"
                           :disabled (not (shape-chooser/both-layers-selected?
                                           @chooser-state))}
            "Set this case"]]]]]

       [common/button {:on-click show!}
        "Set single case"]])))

(defn- shape-selection-settings [state]
  [:div
   [selected-shapes-counter state]
   [all-shapes-selection-buttons state]
   [:div.top30
    "Select available shapes for scrambles by filtering:"
    [layer-selector/layer-selector state]]
   [:div.top30
    [select-single-case-component state]]])

(defn scramble-options [state]
  [common/form
   [inspection-timer-settings/inspection-timer-options (reagent/cursor state [:inspection-timer-settings])]
   [middle-layer-controls/controls (reagent/cursor state [:middle-layer-settings])]])

(defn settings-component [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Cases"])
                  :event-key 1}
    [shape-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :wrench}]
                                               " Scramble options"])
                  :event-key 2}
    [scramble-options state]]])

;; let this module own its state schema by having it defined inside this file
(defn new-state [& {:keys [keybindings]}]
  ;; Allowed shapes are stored as sets of their layers. This makes adding and
  ;; removing them very easy in code.
  (reagent/atom
   {:puzzle nil
    :chosen-shapes nil
    :selected-shapes #{(set ["square" "square"])}
    :scramble-algorithm nil
    :middle-layer-settings (deref (middle-layer-controls/default-state))
    :inspection-timer-settings (deref (inspection-timer-settings/default-state))
    ;; optional
    :keybindings keybindings}))

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

(defn- cheat-sheet-button [state]
  [shortcut-cheat-sheet/cheat-sheet-button (:keybindings @state)])

(defn- action-buttons [state]
  [:div.center
   [repeat-case-button state]
   [new-scramble-button state]
   [inspect-scramble-button state]
   [cheat-sheet-button state]])

(defn- puzzle-preview [state draw-settings]
  (if-let [p (:puzzle @state)]
    [newmonochrome/monochrome-puzzle p draw-settings]
    [common/alert "Select some cases and click 'New scramble' to get started."]))

(defn scramble-component
  ([state]
   [scramble-component state newmonochrome/default-settings])
  ([state draw-settings-map]
   (let [draw-settings (assoc draw-settings-map
                              :size 180)]
     [:div
      [:div.bottom17
       [action-buttons state]]
      [:div.center
       [puzzle-preview state draw-settings]]
      [:div.center
       [scramble-preview state]]
      [settings-component state]])))
