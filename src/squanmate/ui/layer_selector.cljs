(ns squanmate.ui.layer-selector
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as puzzle]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.shapes :as shapes]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.shape-chooser :as shape-chooser]))

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

(defn filtered-possible-shapes [layer-filter]
  (->> shape-combinations/possible-layers
       (filter (fn [shape-names]
                 (some #(= % layer-filter) shape-names)))
       (map #(remove-same-shape-name layer-filter %))
       flatten
       uniquefy))

(defn- shapes-selected? [state-atom shape-names]
  (contains? (:selected-shapes @state-atom) shape-names))

(defn- select-or-deselect! [state-atom shape-names]
  (if (shapes-selected? state-atom shape-names)
    (swap! state-atom update-in [:selected-shapes] disj shape-names)
    (swap! state-atom update-in [:selected-shapes] conj shape-names)))

(defn- shape->layer [shape-name]
  (let [shape (get shapes/all-shapes shape-name)
        layer (-> shape
                  :pieces
                  puzzle/->TopLayer)]
    [(:name shape) layer]))

(defn- layers-selection-component [state filter-shape shape-b-key]
  (let [[name layer] (shape->layer shape-b-key)
        selected? (shapes-selected? state
                                    #{filter-shape shape-b-key})]
    [common/well {:style {:display "inline-block"}
                  :bs-size "small"}
     [common/checkbox {:inline true
                       :checked selected?
                       :on-change #(select-or-deselect! state
                                                        #{filter-shape
                                                          shape-b-key})}

      [:div.center [newmonochrome/layer-component layer {:size 50}]]
      [:div.center name]]]))

(defn- select-all-filtered-shapes! [state filter-shape]
  (let [shape-names (map #(set [filter-shape %])
                         (filtered-possible-shapes filter-shape))]
    (swap! state update :selected-shapes
           into shape-names)))

(defn shape-selection-components [state filter-shape]
  (when filter-shape
    (let [possible-shapes (filtered-possible-shapes filter-shape)]
      [:div
       (into [:div]
             (for [name possible-shapes]
               [layers-selection-component state filter-shape name]))

       [common/button
        {:on-click #(select-all-filtered-shapes! state filter-shape)}
        "Choose all of these"]
       " "
       [common/button "Choose none"]])))

(defn- current-layer-filter [state]
  (-> @state :settings :layer-filter))

(defn layer-selector [state]
  [:div
   [shape-chooser/shape-chooser :state (reagent/cursor state [:settings :layer-filter])]
   [shape-selection-components
    state
    (current-layer-filter state)]])
