(ns squanmate.ui.layer-selector
  (:require [reagent.core :as reagent]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.ui.common :as common]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.ui.shape-preview :as shape-preview]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.puzzle :as puzzle]
            [squanmate.shapes :as shapes]))

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

(defn- shapes-selected? [shape-names state-atom]
  (or (contains? (:selected-shapes @state-atom) shape-names)
      (contains? (:selected-shapes @state-atom) (vec (reverse shape-names)))))

(defn- select-or-deselect [shape-names state-atom]
  (if (shapes-selected? shape-names state-atom)
    (do
      ;; Shape names can be in either order. This is a bit hacky though.. but
      ;; it's the clearest way to handle this, I think.
      (swap! state-atom update-in [:selected-shapes] disj shape-names)
      (swap! state-atom update-in [:selected-shapes] disj (vec (reverse shape-names))))
    (swap! state-atom update-in [:selected-shapes] conj shape-names)) )

(defn- shape->layer [shape-name]
  (let [shape (get shapes/all-shapes shape-name)
        layer (-> shape
                  :pieces
                  puzzle/->TopLayer)]
    [(:name shape) layer]))

(defn- layers-selection-component [state filter-shape shape-b-key]
  (let [[name layer] (shape->layer shape-b-key)
        selected? (shapes-selected? [filter-shape shape-b-key]
                                    state)]
    [common/well {:style {:display "inline-block"}
                  :bs-size "small"}
     [common/checkbox {:inline true
                       :checked selected?
                       :on-change #(select-or-deselect [filter-shape shape-b-key] state)}

      [:div.center [newmonochrome/layer-component layer {:size 50}]]
      [:div.center name]]]))

(defn shape-selection-components [state shape-name]
  (let [possible-shapes (filtered-shape-selections shape-name)]
    [:div
     (when shape-name
       [:div
        (into [:div]
              (for [name possible-shapes]
                [layers-selection-component state shape-name name]))

        [common/button "Choose all of these"]
        " "
        [common/button "Choose none"]])]))

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
                        :on-change #(select-or-deselect [a b] state)}
       (str a " " b)])))
