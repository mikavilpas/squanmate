(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [reagent.core :as reagent]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.ui.common :as common]
            [squanmate.ui.exporting :as exporting]))

(defn- interesting-step? [step-either]
  (either/branch step-either
                 ;; errors must always be shown
                 (constantly true)
                 (fn [s]
                   (let [interesting #{execution/StartingStepResult
                                       execution/RotationStepResult}]
                     (interesting (type s))))))

(defn- last-step? [index total-amount]
  (= index (- total-amount 1)))

(defn- error-component [e]
  [common/panel {:header "Error"
                 :bs-style "danger"}
   (pr-str e)])

(defn algorithm-visualization [puzzle alg-string]
  (let [step-eithers (execution/transformations puzzle alg-string)]
    [:div#visualization
     ;; take up as little space as possible
     {:style {:display "inline-block"}}
     (for [[step-either index] (zipmap step-eithers (range))
           :when (or (interesting-step? step-either)
                     (last-step? index (count step-eithers)))]
       ^{:key (str index)}
       [:div
        (either/branch step-either
                       error-component
                       (fn [step]
                         [newmonochrome/monochrome-puzzle (:puzzle step)]))])]))

(defn- both-layers-present? [puzzle]
  (and (:top-layer puzzle)
       (:bottom-layer puzzle)))

(defn- apply-initial-transformation-alg [puzzle alg-string]
  (let [result-step-either (execution/transformation-result puzzle alg-string)]
    (m/bind result-step-either (comp either/right :puzzle))))

(defn default-alg-visualizer-state []
  (reagent/atom {:puzzle (puzzle/Puzzle. nil nil)
                 :initial-rotation ""
                 :algorithm ""}))

(defn- export-visualization-button []
  [common/button
   {:on-click #(exporting/download-html-node-as-png :id-string "visualization"
                                                    :filename (str "squanmate-" (js/Date.) ".png"))}
   "Export as .PNG"])

(defn alg-visualizer [state]
  [:form
   [:div.form-group.col-xs-8
    [shape-chooser/puzzle-chooser state]]

   [:div.form-group
    [:div.row
     [:div.col-xs-12
      [common/input-box (reagent/cursor state [:initial-rotation]) "Initial rotation"]]]
    [:div.row
     [:div.col-xs-12
      [common/input-box (reagent/cursor state [:algorithm]) "Algorithm"]]]]

   [:div.form-group
    [:div.row
     [:div.col-xs-12
      (when-let [initial-puzzle (and (both-layers-present? (:puzzle @state))
                                     (apply-initial-transformation-alg (:puzzle @state)
                                                                       (:initial-rotation @state)))]
        (either/branch
         initial-puzzle
         error-component
         (fn [initial-puzzle]
           [:div
            [:div.row
             [:div.col-xs-3
              [export-visualization-button]]]
            [:div.row.col-xs-12
             [algorithm-visualization initial-puzzle (:algorithm @state)]]])))]]]])
