(ns squanmate.ui.parity-analysis
  (:require [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.ui.common :as common]))

(defn- parity-tag [parity?]
  (if parity?
    [common/label {:bs-style :info} "Odd parity count"]))

(defn- parity-factor [section-name piece-order]
  (let [parity-count (:parity-count piece-order)]
    [:tr
     [:td [common/help-block section-name]]
     [:td
      ;; "TODO show colors"
      (when (> parity-count 1)
        [:span {:class "piece-count-badge"}
         [common/badge parity-count]])
      " "
      (if (odd? parity-count)
        [common/label {:bs-style :info} "Odd"]
        [common/label {:bs-style :warning} "Even"])]]))

(defn- parity-count-analysis [[parity? pc]]
  (js/console.log pc)
  [:div
   [parity-tag parity?]
   [:table.parity-count-analysis
    [:col] [:col]
    [:tbody
     [parity-factor "Top corners" (:top-corner-order pc)]
     [parity-factor "Top edges" (:top-edge-order pc)]

     [parity-factor "Bottom corners" (:bottom-corner-order pc)]
     [parity-factor "Bottom edges" (:bottom-edge-order pc)]

     [parity-factor "Top odd positioned edges" (:top-edges-in-odd-edge-positions pc)]
     [parity-factor "Top odd positioned corners" (:top-corners-in-odd-corner-positions pc)]]]])

(defn parity-analysis [puzzle]
  (let [pc (parity-counter/parity-count puzzle)]
    [parity-count-analysis pc]))

;; todo have the colors in some module, so they are shared across all modules.
