(ns squanmate.ui.layer-selector
  (:require [reagent.core :as reagent]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.ui.common :as common]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.ui.shape-preview :as shape-preview]))

(defn- uniquefy [things]
  (-> things set))

(defn- remove-same-shape-name [name [a b]]
  (if (= name a b)
    ;; This is a case where it's possible to have e.g. square square. So the
    ;; name is given twice, and must be returned too
    name
    ;; This is a case e.g. square kite, which means only the kite shape is
    ;; valuable to the caller
    (first (filter #(not (= name %)) [a b]))))

(defn filtered-shape-selections [layer-filter]
  (->> shape-combinations/possible-layers
       (filter (fn [shape-names]
                 (some #(= % layer-filter) shape-names)))
       (map #(remove-same-shape-name layer-filter %))
       flatten
       uniquefy))

(defn shape-selection-components [state layer-filter]
  (let [possible-shapes (filtered-shape-selections layer-filter)]
    (into [:div]
          (for [name possible-shapes]
            [shape-preview/shape-preview-for-shape-name name {:size 70}]))))

(defn layer-selector [state]
  [:div
   [shape-chooser/shape-chooser :state (reagent/cursor state [:settings :layer-filter])]
   [shape-selection-components
    state
    (-> @state :settings :layer-filter)]])

(comment
  (for [[a b] shape-combinations/possible-layers]
    (let [id (str a " " b)]
      [common/checkbox {:id id
                        :inline true
                        :checked (contains? (:selected-shapes @state) [a b])
                        :on-change #(checkbox-handler [a b] state)}
       (str a " " b)])))
