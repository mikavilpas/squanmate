(ns squanmate.ui.count-position-finder
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.parser :as parser]
            [squanmate.rotation :as rotation]
            [squanmate.services.count-positions :as count-positions]
            [squanmate.shapes :as shapes]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.initial-rotation-adjuster :as ira]
            [squanmate.ui.shape-chooser :as shape-chooser]))

(defn default-state []
  (reagent/atom {:initial-rotation "(0,0)"
                 :dummy-algorithm ""}))

(defn- parse-top-rotation [initial-rotation]
  (m/mlet [alg-steps (parser/parse initial-rotation)]
          (m/return (:top-amount (first alg-steps)))))

(defn- make-layer [layer-name initial-rotation]
  (when layer-name
    (let [[_name layer]
          (shapes/shape->name-&-top-layer layer-name)

          rotated-layer-either
          (m/mlet [top-rotation (parse-top-rotation initial-rotation)
                   rotation-result (rotation/rotate-layer layer top-rotation)]
                  (m/return rotation-result))]
      (either/branch rotated-layer-either
                     (fn [error]
                       (throw (js/Error.
                               "cannot create layer " layer-name
                               " with initial-rotation " initial-rotation
                               ". Error: " error)))
                     (fn [layer-result]
                       layer-result)))))

(defn- visualization [layer]
  (let [count-position-groups (count-positions/count-positions layer)
        visual-settings {:size 200
                         :count-positions count-position-groups}]
    [newmonochrome/layer-component layer visual-settings]))

(defn count-position-finder [state]
  [:div
   [shape-chooser/shape-chooser :state (reagent/cursor state [:settings :layer-name])]
   [:div.center
    (when-let [layer (make-layer (-> @state :settings :layer-name)
                                 (:initial-rotation @state))]
      [:div
       [visualization layer]
       [:div.center
        [ira/layer-rotation-controls layer
         (reagent/cursor state [:initial-rotation])
         (reagent/cursor state [:dummy-algorithm])]]])]])
