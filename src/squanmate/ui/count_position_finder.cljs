(ns squanmate.ui.count-position-finder
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [reagent.core :as reagent]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.shapes :as shapes]
            [squanmate.services.count-positions :as count-positions]))

(defn default-state []
  (reagent/atom {}))

(defn- make-layer [layer-name]
  (when layer-name
    (let [[_name layer] (shapes/shape->name-&-top-layer layer-name)]
      layer)))

(defn count-position-finder [state]
  [:div
   [shape-chooser/shape-chooser :state (reagent/cursor state [:settings :layer-name])]
   [:div.center
    (when-let [layer (make-layer (-> @state :settings :layer-name))]
      (let [count-position-groups (count-positions/count-positions layer)
            visual-settings {:size 200
                             :count-positions count-position-groups}]
        [newmonochrome/layer-component layer visual-settings]))]])
