(ns squanmate.ui.shortcut-cheat-sheet
  (:require [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

(defn- cheat-sheet [bindings]
  [:div
   [:h3 "Keyboard shortcuts"]
   [:table.table.top17
    [:tbody
     (for [b bindings]
       ^{:key (str "binding-" (pr-str b))}
       [:tr
        [:td [:pre (:key-combination b)]]
        [:td (:description b)]])]]])

(defn cheat-sheet-button [bindings]
  [common/overlay-trigger
   {:overlay (reagent/as-element
              [common/popover {:id "keyboard-cheat-sheet"}
               [cheat-sheet bindings]])
    :placement :left
    :trigger :click}
   [common/button
    [common/glyphicon {:glyph :th}]]])
