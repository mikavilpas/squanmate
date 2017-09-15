(ns squanmate.ui.layer-selector
  (:require [clojure.set :as set]
            [reagent.core :as reagent]
            [squanmate.puzzle :as puzzle]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.shapes :as shapes]
            [squanmate.ui.common :as common]
            [squanmate.ui.shape-chooser :as shape-chooser]))

(defn- shapes-selected? [state-atom shape-names]
  (contains? (:selected-shapes @state-atom) shape-names))

(defn- select-or-deselect! [state-atom shape-names]
  (if (shapes-selected? state-atom shape-names)
    (swap! state-atom update-in [:selected-shapes] disj shape-names)
    (swap! state-atom update-in [:selected-shapes] conj shape-names)))

(defn- layers-selection-component [state filter-shape shape-b-key]
  (let [[name layer] (shapes/shape->name-&-top-layer shape-b-key)
        selected? (shapes-selected? state
                                    #{filter-shape shape-b-key})]
    [common/well {:style {:display "inline-block"}
                  :bs-size "small"}
     [common/checkbox {:inline true
                       :checked selected?
                       :on-change #(select-or-deselect! state
                                                        #{filter-shape
                                                          shape-b-key})}

      [:div.center
       [:img {:src (common/shape-preview-image-url shape-b-key)
              :height "80px"}]]

      [:div.center
       name]]]))

(defn- select-all-filtered-shapes! [state filter-shape]
  (let [shape-names (map #(set [filter-shape %])
                         (shape-combinations/filtered-possible-shapes filter-shape))]
    (swap! state update :selected-shapes
           into shape-names)))

(defn- select-no-filtered-shapes! [state filter-shape]
  (let [shape-names (map #(set [filter-shape %])
                         (shape-combinations/filtered-possible-shapes filter-shape))]
    (swap! state update :selected-shapes
           #(set/difference % shape-names))))

(defn shape-selection-components [state filter-shape]
  (when filter-shape
    (let [possible-shapes (shape-combinations/filtered-possible-shapes filter-shape)]
      [:div
       (into [:div]
             (for [name possible-shapes]
               [layers-selection-component state filter-shape name]))

       [common/button
        {:on-click #(select-all-filtered-shapes! state filter-shape)}
        "Choose all of these"]
       " "
       [common/button
        {:on-click #(select-no-filtered-shapes! state filter-shape)}
        "Choose none"]])))

(defn- current-layer-filter [state]
  (-> @state :settings :layer-filter))

(defn layer-selector [state]
  [:div
   [shape-chooser/shape-chooser :state (reagent/cursor state [:settings :layer-filter])]
   [shape-selection-components
    state
    (current-layer-filter state)]])
