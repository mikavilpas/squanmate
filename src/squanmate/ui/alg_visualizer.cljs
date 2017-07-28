(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [reagent.core :as reagent]
            [squanmate.ui.parity :as parity]
            [squanmate.ui.shape-chooser :as shape-chooser]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.ui.common :as common]
            [squanmate.ui.exporting :as exporting]
            [clojure.string :as str]))

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

(defn algorithm-visualization

  ([puzzle alg-string]
   [algorithm-visualization puzzle alg-string {}])

  ([puzzle alg-string settings]
   (let [step-eithers (execution/transformations puzzle alg-string)]
     [:div
      [parity/alg-parity-switched?-component step-eithers]
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
   {:on-click #(exporting/download-html-node-as-png :id-string "visualization"
                                                    :filename (str "squanmate-" (js/Date.) ".png"))}
   "Export as .PNG"])

;; todo could get this from secretary config atom
(def ^:private route-prefix "#/")

(defn- set-route! [route-str]
  (let [current-route (.-href js/window.location)
        [base route] (str/split current-route route-prefix)
        new-route (str/join [base route-prefix route-str])]
    (set! js/window.location.href new-route)))

(def ^:private encode js/encodeURIComponent)

(defn- alg-or-zero [alg]
  (if (= "" alg)
    "0"
    alg))

(defn- set-link-to-visualization [state]
  (set-route! (str/join "/" ["shape-visualizer"
                             (-> state :puzzle-chooser-layer-names :top)
                             (-> state :puzzle-chooser-layer-names :bottom)
                             (encode (-> state :initial-rotation alg-or-zero))
                             (encode (-> state :algorithm alg-or-zero))])))

(defn- link-to-this-visualization [state]
  [common/button
   {:on-click #(set-link-to-visualization state)}
   "Link to this visualization"])

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
              [link-to-this-visualization @state]
              [export-visualization-button]]]
            [:div.row.col-xs-12
             [algorithm-visualization initial-puzzle (:algorithm @state)]]])))]]]])
