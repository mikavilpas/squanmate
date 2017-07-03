(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [reagent.core :as reagent]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]))

(defn- interesting-step? [step-either]
  (either/branch step-either
                 ;; errors must always be shown
                 (constantly true)
                 (fn [s]
                   (let [interesting #{execution/StartingStepResult
                                       execution/SliceStepResult}]
                     (interesting (type s))))))

(defn algorithm-visualization [puzzle alg-string]
  (let [step-eithers (execution/transformations puzzle alg-string)]
    [:div
     (for [[step-either index] (zipmap step-eithers (range))
           :when (interesting-step? step-either)]
       ^{:key (str index)}
       [:div
        (either/branch step-either
                       (fn [step]
                         [:div (pr-str step)])

                       (fn [step]
                         [monochrome/monochrome-puzzle (:puzzle step)]))])]))

(defn alg-visualizer []
  (let [state (reagent/atom {:puzzle (puzzle/Puzzle. nil nil)
                             :algorithm ""})]
    (fn render []
      [:div.row
       [:form
        [:div.form-group.col-xs-8
         [shape-chooser/puzzle-chooser state]]

        [:div.form-group
         [:input.form-control {:on-change #(swap! state assoc-in [:algorithm]
                                                  (-> % .-target .-value))
                               :placeholder "Algorithm"}]]
        [:div.col-xs-5
         (let [top (get-in @state [:puzzle :top-layer])
               bottom (get-in @state [:puzzle :bottom-layer])]
           (when (and top bottom)
             [:div
              [algorithm-visualization (:puzzle @state) (:algorithm @state)]]))]]])))
