(ns squanmate.ui.shape-chooser
  (:require [cljsjs.react-select]
            [squanmate.shapes :as shapes]
            [reagent.core :as reagent]))

;; based on example code from
;; https://gist.github.com/pesterhazy/4a4198a9cc040bf6fe13a476f25bac2c

(defn- select
  "Select based on an atom/cursor. Pass as state"
  [{:keys [state]
    :as props}]
  [:> js/Select
   (-> props
       (dissoc state)
       (assoc :value @state
              :on-change (fn [x]
                           (reset! state (when x (.-value x))))))])

(defn make-value [& {:keys [id label]}]
  {:value id :label label})

(defn chooser [& {:keys [options state]}]
  [select {:state state
           :options (clj->js options)}])

(defn shape-chooser []
  (let [state (reagent/atom nil)
        options (for [[id s] shapes/all-shapes]
                  (make-value :id id
                              :label (:name s)))]
    [chooser :options options, :state state]))
