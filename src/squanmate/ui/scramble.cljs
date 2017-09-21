(ns squanmate.ui.scramble
  (:require [reagent.core :as reagent]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [clojure.string :as str]))

(defn default-state []
  (reagent/atom {:puzzle nil
                 :scramble-algorithm nil}))

(defn- puzzle-view [state]
  [newmonochrome/monochrome-puzzle (:puzzle @state) {:monochrome? false}]
  )

(defn import-alg-view [state]
  (let [given-alg (reagent/cursor state [:scramble-algorithm])]
    [:div
     [common/input-box given-alg "Enter your scramble"]
     (when-not (str/blank? @given-alg)
       [:div.center.top17
        [common/button {:bs-size "large"
                        :bs-style "success"} "Import"]])]))

(defn component [state]
  [:div
   (if (:puzzle @state)
     [puzzle-view state]
     [import-alg-view state])])
