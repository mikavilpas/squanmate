(ns squanmate.ui.scramble
  (:require [reagent.core :as reagent]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [clojure.string :as str]
            [squanmate.alg.execution :as execution]
            [squanmate.puzzle :as puzzle]
            [cats.monad.either :as either]))

(defn default-state []
  (reagent/atom {:imported? false
                 :scramble-algorithm nil}))

(defn- invalid-scramble [scramble-alg error]
  [:div "Invalid scramble: " (pr-str error)])

(defn- show-scramble [puzzle state]
  [:div.center
   [newmonochrome/monochrome-puzzle puzzle {:monochrome? false
                                            :size 200}]])

(defn- puzzle-view [state]
  (let [scramble-alg (:scramble-algorithm @state)
        puzzle-either (execution/transformation-result puzzle/square-square
                                                       scramble-alg)]
    (either/branch puzzle-either
                   (fn [error]
                     [invalid-scramble scramble-alg error])
                   (fn [transformation-result]
                     [show-scramble (:puzzle transformation-result) state]))))

(defn- mark-alg-imported [state]
  (swap! state assoc :imported? true))

(defn import-alg-view [state]
  (let [given-alg (reagent/cursor state [:scramble-algorithm])]
    [:div
     [common/input-box given-alg "Enter your scramble"]
     (when-not (str/blank? @given-alg)
       [:div.center.top17
        [common/button {:bs-size "large"
                        :bs-style "success"
                        :on-click #(mark-alg-imported state)}
         "Import"]])]))

(defn component [state]
  [:div
   (if (:imported? @state)
     [puzzle-view state]
     [import-alg-view state])])
