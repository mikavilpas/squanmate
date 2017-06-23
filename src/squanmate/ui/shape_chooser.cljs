(ns squanmate.ui.shape-chooser
  (:require [cljsjs.react-select]
            [clojure.walk :as walk]))

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

(defn chooser [& {:keys [selectable-values state]}]
  [select {:state state
           :options (clj->js selectable-values)}])

(defn make-value [& {:keys [id label]}]
  {:value id :label label})
