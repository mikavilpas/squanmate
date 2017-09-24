(ns squanmate.ui.parity-analysis
  (:require [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.ui.common :as common]))

(defn- parity-tag [parity?]
  (if parity?
    [common/label {:bs-style :info} "Odd parity count"]
    [common/label {:bs-style :warning} "Even parity count"]))

(defn- parity-factor
  ([section-name piece-order]
   (parity-factor section-name piece-order false))
  ([section-name piece-order show-number?]
   (let [parity-count (:parity-count piece-order)]
     [:tr
      [:td [common/help-block section-name]]
      [:td
       ;; "TODO show colors"
       (when show-number?
         [:span {:class "piece-count-badge"}
          [common/badge parity-count]])
       " "
       (if (odd? parity-count)
         [common/label {:bs-style :info} "Odd"]
         [common/label {:bs-style :warning} "Even"])]])))

(defn- parity-count-analysis [[parity? pc]]
  (js/console.log parity? pc)
  [:div
   [parity-tag parity?]
   [:table.parity-count-analysis
    [:tbody
     [parity-factor "Top corners" (:top-corner-order pc)]
     [parity-factor "Top edges" (:top-edge-order pc)]

     [parity-factor "Bottom corners" (:bottom-corner-order pc)]
     [parity-factor "Bottom edges" (:bottom-edge-order pc)]

     [parity-factor "Top odd positioned edges" (:top-edges-in-odd-edge-positions pc) true]
     [parity-factor "Top odd positioned corners" (:top-corners-in-odd-corner-positions pc) true]]]])

(defn parity-analysis [puzzle]
  (let [pc (parity-counter/parity-count puzzle)]
    [parity-count-analysis pc]))

;; todo have the colors in some module, so they are shared across all modules.
