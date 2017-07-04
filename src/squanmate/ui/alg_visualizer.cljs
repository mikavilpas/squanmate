(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.monochrome :as monochrome]
            [reagent.core :as reagent]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [cats.core :as m]
            [cljsjs.react-bootstrap]))

(defn- interesting-step? [step-either]
  (either/branch step-either
                 ;; errors must always be shown
                 (constantly true)
                 (fn [s]
                   (let [interesting #{execution/StartingStepResult
                                       execution/RotationStepResult}]
                     (interesting (type s))))))

(def panel (reagent/adapt-react-class js/ReactBootstrap.Panel))

(defn- error-component [e]
  [panel {:header "Error"
          :bs-style "danger"}
   (pr-str e)])

(defn algorithm-visualization [puzzle alg-string]
  (let [step-eithers (execution/transformations puzzle alg-string)]
    [:div
     (for [[step-either index] (zipmap step-eithers (range))
           :when (interesting-step? step-either)]
       ^{:key (str index)}
       [:div
        (either/branch step-either
                       error-component
                       (fn [step]
                         [monochrome/monochrome-puzzle (:puzzle step)]))])]))

(defn- input-box [string-state placeholder]
  [:input.form-control {:on-change #(reset! string-state (-> % .-target .-value))
                        :placeholder placeholder}])

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

(defn alg-visualizer [state]
  [:div.row
   [:form
    [:div.form-group.col-xs-8
     [shape-chooser/puzzle-chooser (reagent/cursor state [:puzzle])]]

    [:div.form-group
     [input-box (reagent/cursor state [:initial-rotation]) "Initial rotation"]
     [input-box (reagent/cursor state [:algorithm]) "Algorithm"]]
    [:div
     (when-let [initial-puzzle (and (both-layers-present? (:puzzle @state))
                                    (apply-initial-transformation-alg (:puzzle @state)
                                                                      (:initial-rotation @state)))]
       (either/branch
        initial-puzzle
        error-component
        (fn [initial-puzzle]
          [algorithm-visualization initial-puzzle (:algorithm @state)])))]]])
