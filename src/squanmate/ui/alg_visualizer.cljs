(ns squanmate.ui.alg-visualizer
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as puzzle]
            [squanmate.shapes :as shapes]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.initial-rotation-adjuster :as initial-rotation-adjuster]
            [squanmate.ui.parity :as parity]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.utils.either-utils :as eu]
            [squanmate.utils.route-utils :as route-utils]
            [squanmate.utils.export-utils :as export-utils]))

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

(defn ends-at-cubeshape? [step-eithers]
  (-> step-eithers
      last
      m/extract
      :puzzle
      shapes/puzzle-layer-shape-names
      (=
       ["square" "square"])))

(defn algorithm-visualization

  ([puzzle alg-string]
   [algorithm-visualization puzzle alg-string {}])

  ([puzzle alg-string settings]
   (let [step-eithers (execution/transformations puzzle alg-string)]
     [:div.top17
      (when (and (execution/successful-transformations? step-eithers)
                 (ends-at-cubeshape? step-eithers))
        [:div [parity/alg-parity-switched-at-cubeshape?-component alg-string]])

      ;; using into [] removes a react warning about a missing unique key
      (into [:div#visualization
             ;; take up as little space as possible
             {:style {:display "inline-block"}}]
            (for [[index step-either] (zipmap (range) step-eithers)
                  :when (or (interesting-step? step-either)
                            (last-step? index (count step-eithers)))]
              [:div
               (either/branch step-either
                              error-component
                              (fn [step]
                                [newmonochrome/monochrome-puzzle (:puzzle step) settings]))]))])))

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
   {:on-click #(export-utils/download-html-node-as-png :id-string "visualization"
                                                       :filename (str "squanmate-" (js/Date.) ".png"))}
   "Export as .PNG"])

(def ^:private encode js/encodeURIComponent)

(defn- alg-or-zero [alg]
  (if (= "" alg)
    "0"
    alg))

(defn- set-link-to-visualization [state]
  (route-utils/set-route!
   (str/join "/" ["shape-visualizer"
                  (-> state :puzzle-chooser-layer-names :top)
                  (-> state :puzzle-chooser-layer-names :bottom)
                  (encode (-> state :initial-rotation alg-or-zero))
                  (encode (-> state :algorithm alg-or-zero))])))

(defn- link-to-this-visualization [state]
  [common/button
   {:on-click #(set-link-to-visualization state)}
   "Link to this visualization"])

(defn alg-visualizer [state]
  (let [top-layer-name (-> @state :puzzle-chooser-layer-names :top)
        bottom-layer-name (-> @state :puzzle-chooser-layer-names :bottom)
        initial-puzzle (apply-initial-transformation-alg (:puzzle @state)
                                                         (:initial-rotation @state))]
    [:form.container

     [:div.row.form-group
      [:div.col-xs-8
       [shape-chooser/puzzle-chooser state]]
      [:div.col-xs-4 (when (or top-layer-name
                               bottom-layer-name)
                       [shape-chooser/swap-layers-button state])]]

     [:div.row.form-group
      [:div.col-xs-8
       [common/input-box (reagent/cursor state [:initial-rotation]) "Initial rotation"]
       [common/input-box (reagent/cursor state [:algorithm]) "Algorithm"]]]

     [:div.row.form-group
      [:div.col-xs-8
       (when (both-layers-present? (:puzzle @state))
         [:div
          (either/branch
           initial-puzzle
           error-component
           (fn [initial-puzzle]
             [:div
              [:div.row
               [:div.col-xs-3
                [link-to-this-visualization @state]
                [export-visualization-button]]]
              [:div.row.col-xs-8
               [algorithm-visualization initial-puzzle (:algorithm @state)]]]))])]
      [:div.col-xs-4.pull-right
       (eu/when-right initial-puzzle
         (fn [p]
           [initial-rotation-adjuster/rotation-adjuster
            p
            (reagent/cursor state [:initial-rotation])
            (reagent/cursor state [:algorithm])]))]]]))
