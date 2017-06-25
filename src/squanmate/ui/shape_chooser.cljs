(ns squanmate.ui.shape-chooser
  (:require [cljsjs.react-select]
            [squanmate.shapes :as shapes]
            [reagent.core :as reagent]
            [squanmate.ui.drawing.monochrome :as monochrome]))

;; based on example code from
;; https://gist.github.com/pesterhazy/4a4198a9cc040bf6fe13a476f25bac2c

(defn- render-shape-option [s]
  (let [shape-key (.-value s)
        shape (get shapes/all-shapes shape-key)]
    (reagent/as-element
     [:div (when shape
             [monochrome/layer-component shape :size 30])
      (.-label s)])))

(defn- select
  "Select based on an atom/cursor. Pass as state"
  [{:keys [state]
    :as props}]
  [:> js/Select
   (-> props
       (dissoc state)
       (assoc :value @state
              :on-change (fn [x]
                           (reset! state (when x (.-value x))))
              :option-renderer render-shape-option
              :value-renderer render-shape-option))])

(defn make-value [& {:keys [id label]}]
  {:value id :label label})

(defn chooser [& {:keys [options state]}]
  [select {:state state
           :options (clj->js options)}])

(defn shape-chooser [& {:keys [state]}]
  (let [options (vec (for [[id s] shapes/all-shapes]
                       (make-value :id id
                                   :label (:name s))))]
    [chooser :options options :state state]))
