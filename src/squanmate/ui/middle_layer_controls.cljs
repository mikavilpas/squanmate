(ns squanmate.ui.middle-layer-controls
  (:require [reagent.core :as reagent]
            [squanmate.ui.common :as common]))

(defn default-state []
  (reagent/atom {:middle-layer-settings {:flip-mode :random}}))

(defn- radio-for [state-atom flip-mode label]
  [common/radio {:checked (-> @state-atom
                              :middle-layer-settings :flip-mode
                              (= flip-mode))
                 :name "middle-layer"
                 :on-change #(swap! state-atom assoc-in [:middle-layer-settings :flip-mode] flip-mode)}
   label])

(defn controls [state]
  [common/form
   [common/form-group
    [common/control-label "Flip middle layer"]
    [common/help-block [common/glyphicon {:glyph :info-sign}]
     " The scramble will cause the middle layer to be flipped according to your choice"]
    [radio-for state :random "randomly"]
    [radio-for state :never "never"]
    [radio-for state :always "always"]]])
