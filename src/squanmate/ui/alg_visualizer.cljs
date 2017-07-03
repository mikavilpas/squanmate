(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [reagent.core :as reagent]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [cats.core :as m]))

(defn- interesting-step? [step-either]
  (either/branch step-either
                 ;; errors must always be shown
                 (constantly true)
                 (fn [s]
                   (let [interesting #{execution/StartingStepResult
                                       execution/SliceStepResult}]
                     (interesting (type s))))))

(defn- show-error [e]
  [:div (pr-str e)])

(defn algorithm-visualization [puzzle alg-string]
  (let [step-eithers (execution/transformations puzzle alg-string)]
    [:div
     (for [[step-either index] (zipmap step-eithers (range))
           :when (interesting-step? step-either)]
       ^{:key (str index)}
       [:div
        (either/branch step-either
                       show-error
                       (fn [step]
                         [monochrome/monochrome-puzzle (:puzzle step)]))])]))

(defn- input-box [string-state placeholder]
  [:input.form-control {:on-change #(reset! string-state (-> % .-target .-value))
                        :placeholder placeholder}])

(defn- both-layers-present? [puzzle]
  (and (:top-layer puzzle)
       (:bottom-layer puzzle)))

(defn- apply-initial-transformation-alg [puzzle alg-string]
  (let [step-eithers (execution/transformations puzzle alg-string)
        result-either (last step-eithers)]
    (either/branch-right result-either
                         (fn [result]
                           (either/right (:puzzle result))))))

(defn alg-visualizer []
  (let [state (reagent/atom {:puzzle (puzzle/Puzzle. nil nil)
                             :initial-rotation ""
                             :algorithm ""})]
    (fn render []
      [:div.row
       [:form
        [:div.form-group.col-xs-8
         [shape-chooser/puzzle-chooser state]]

        [:div.form-group
         [input-box (reagent/cursor state [:initial-rotation]) "Initial rotation"]
         [input-box (reagent/cursor state [:algorithm]) "Algorithm"]]
        [:div.col-xs-5
         (when-let [initial-puzzle (and (both-layers-present? (:puzzle @state))
                                        (apply-initial-transformation-alg (:puzzle @state)
                                                                          (:initial-rotation @state)))]
           (either/branch
            initial-puzzle
            show-error
            (fn [initial-puzzle]
              [algorithm-visualization initial-puzzle (:algorithm @state)])))]]])))
