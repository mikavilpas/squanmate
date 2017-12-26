(ns squanmate.ui.parity-analysis
  (:require [squanmate.alg.parity-counter :as parity-counter]
            [squanmate.ui.common :as common]
            [squanmate.services.color-converter :as color-converter]))

(defn- parity-tag [parity?]
  (if parity?
    [common/label {:bs-style :info} "Odd parity count"]
    [common/label {:bs-style :warning} "Even parity count"]))

(defn- odd-positioned-pieces-factor [section-name piece-order]
  (let [parity-count (:parity-count piece-order)]
    [:tr
     [:td [common/help-block section-name]]
     [:td.center
      [:span {:class "piece-count-badge"}
       [common/badge parity-count]]]
     [:td
      (if (odd? parity-count)
        [common/label {:bs-style :info} "Odd"]
        [common/label {:bs-style :warning} "Even"])]]))

(defn parity-color->hex [color-settings piece]
  ;; Only one side of the piece affects parity.
  ;; This is always the first side when going clockwise, so it is always the
  ;; first side (that is, :a)
  (let [side (-> piece :colors :a)
        color (get color-settings side)]
    (color-converter/color->hex color)))

(defn- colors [color-settings pieces]
  [:div
   (for [p pieces]
     ^{:key (pr-str p)}
     [:div.color {:style {:background-color (parity-color->hex color-settings
                                                               p)}}])])

(defn- color-sequence-factor [color-settings section-name piece-order]
  (let [parity-count (:parity-count piece-order)]
    [:tr
     [:td [common/help-block section-name]]
     [:td
      [:div.center
       [colors color-settings (:pieces piece-order)]]]
     [:td
      (if (odd? parity-count)
        [common/label {:bs-style :info} "Odd"]
        [common/label {:bs-style :warning} "Even"])]]))

(defn- parity-disclaimer []
  [common/help-block
   [:span.col-xs-1
    [common/glyphicon {:glyph :info-sign}]]
   [:span.col-xs-11
    "The parity is calculated for Squanmate's default color scheme (orange blue
   red green clockwise). If your puzzle has a different color scheme, the total
   parity will be correct but the color sequences' parities might be the wrong
   way round."]])

(defn- parity-count-analysis [[parity? pc] color-settings]
  [:div
   [parity-tag parity?]
   [parity-disclaimer]
   [:table.parity-count-analysis
    (let [color-factor (partial color-sequence-factor color-settings)]
      [:tbody
       [color-factor "Top corners" (:top-corner-order pc)]
       [color-factor "Top edges" (:top-edge-order pc)]

       [color-factor "Bottom corners" (:bottom-corner-order pc)]
       [color-factor "Bottom edges" (:bottom-edge-order pc)]

       [odd-positioned-pieces-factor "Top odd positioned edges" (:top-edges-in-odd-edge-positions pc)]
       [odd-positioned-pieces-factor "Top odd positioned corners" (:top-corners-in-odd-corner-positions pc)]])]])

(defn parity-analysis [puzzle color-settings]
  (let [pc (parity-counter/parity-count puzzle)]
    [parity-count-analysis pc color-settings]))
