(ns squanmate.ui.shape-chooser
  (:require [cljsjs.react-select]
            [squanmate.shapes :as shapes]
            [reagent.core :as reagent]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.puzzle :as puzzle]))

;; based on example code from
;; https://gist.github.com/pesterhazy/4a4198a9cc040bf6fe13a476f25bac2c

(defn- render-shape-option [s]
  (let [shape-key (.-value s)]
    (reagent/as-element
     [:div [:img {:src (str "../../img/shape-thumbnails/" shape-key ".png")
                  :style {:margin-top "-3px" }
                  :height "40px"}]
      (.-label s)])))

(def select-component (reagent/adapt-react-class js/Select))

;; something is amiss here!!
(defn- select
  "Select based on an atom/cursor. Pass as state"
  [{:keys [state]
    :as initial-props}]
  (let [props (-> initial-props
                (dissoc state)
                (assoc :value @state
                       :on-change (fn [x]
                                    (reset! state (when x (.-value x))))
                       :option-renderer render-shape-option
                       :value-renderer render-shape-option))]
    [select-component props]))

(defn make-value [& {:keys [id label]}]
  {:value id :label label})

(defn chooser [& {:keys [options state]}]
  [select {:state state
           :options (clj->js options)}])

(defn shape-chooser [& {:keys [state]}]
  (let [options (vec (for [[id s] shapes/all-shapes]
                       (make-value :id id
                                   :label (:name s))))]
    [chooser :options options :state state]))

(defn- layers-from-layer-names [top-name bottom-name]
  (when (and top-name bottom-name)
    (let [top-layer (-> shapes/all-shapes
                        (get top-name)
                        :pieces
                        puzzle/->TopLayer)
          bottom-layer (-> shapes/all-shapes
                           (get bottom-name)
                           :pieces
                           puzzle/->BottomLayer)]
      [top-layer bottom-layer])))

(defn puzzle-chooser [state]
  ;; store layer names in the given state so they are restored when navigating
  ;; to the page again
  (let [layer-names (reagent/cursor state [:puzzle-chooser-layer-names])]
    (fn render [state]
      (when-let [[top bottom] (layers-from-layer-names (:top @layer-names)
                                                       (:bottom @layer-names))]
        (swap! state assoc-in [:puzzle :top-layer] top)
        (swap! state assoc-in [:puzzle :bottom-layer] bottom))
      [:div.row
       [shape-chooser :state (reagent/cursor layer-names [:top])]
       [shape-chooser :state (reagent/cursor layer-names [:bottom])]])))
