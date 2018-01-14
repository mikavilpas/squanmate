(ns squanmate.ui.shape-chooser
  (:require [cljsjs.react-select]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.puzzle :as puzzle]
            [squanmate.services.shape-combinations :as shape-combinations]
            [squanmate.services.shapes :as shapes]
            [squanmate.ui.common :as common]))

(defn get-both-layers-or-nil [state-map]
  (let [top (-> state-map :puzzle-chooser-layer-names :top)
        bottom (-> state-map :puzzle-chooser-layer-names :bottom)]
    (when (and top bottom)
      [top bottom])))

;; alias
(def both-layers-selected? get-both-layers-or-nil)

;; based on example code from
;; https://gist.github.com/pesterhazy/4a4198a9cc040bf6fe13a476f25bac2c

(defn- render-shape-option [s]
  (let [shape-key (.-value s)]
    (reagent/as-element
     [:div [:img {:src (common/shape-preview-image-url shape-key)
                  :style {:margin-top "-3px" }
                  :height "40px"}]
      (.-label s)])))

(def select-component (reagent/adapt-react-class js/Select))

(defn- select
  "Select based on an atom/cursor. Pass as state"
  [{:keys [state callback]
    :as initial-props}]
  (let [props (-> initial-props
                  (dissoc state)
                  (assoc :value @state
                         :on-change (fn [x]
                                      (reset! state (when x (.-value x)))
                                      (when callback
                                        (callback (when x (.-value x)))))
                         :option-renderer render-shape-option
                         :value-renderer render-shape-option))]
    [select-component props]))

(defn make-value [& {:keys [id label]}]
  {:value id :label label})

(defn chooser [& {:keys [options state callback]}]
  [select {:state state
           :options (clj->js options)
           :callback callback}])

(defn- make-shape-options [possible-shapes]
  (vec (for [[id s] (seq possible-shapes)]
         (make-value :id id
                     :label (:name s)))))

(defn shape-chooser [& {:keys [state options callback]}]
  (let [options (or options
                    (make-shape-options shapes/all-shapes))]
    [chooser :options options :state state :callback callback]))

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

(defn- possible-shape-options-for-layer [other-layer-shape-name]
  (let [options (->> other-layer-shape-name
                     shape-combinations/filtered-possible-shapes
                     (map (fn [id]
                            [id (get shapes/all-shapes id)]))
                     make-shape-options)]
    (if (empty? options)
      (make-shape-options shapes/all-shapes)
      options)))

(defn puzzle-chooser [state]
  ;; store layer names in the given state so they are restored when navigating
  ;; to the page again
  (let [layer-names (reagent/cursor state [:puzzle-chooser-layer-names])]
    (fn render [state]
      [:div
       [shape-chooser :state (reagent/cursor layer-names [:top])
        :options (possible-shape-options-for-layer (:bottom @layer-names))]
       [shape-chooser :state (reagent/cursor layer-names [:bottom])
        :options (possible-shape-options-for-layer (:top @layer-names))]])))

(defn- swap-top-and-bottom-layers [state]
  (let [top (-> state :puzzle-chooser-layer-names :top)
        bottom (-> state :puzzle-chooser-layer-names :bottom)]
    ;; notice: switch layers
    (-> state
        (assoc-in [:puzzle-chooser-layer-names :top] bottom)
        (assoc-in [:puzzle-chooser-layer-names :bottom] top))))

(defn swap-layers [state]
  (swap! state swap-top-and-bottom-layers)
  (swap! state update :initial-rotation manipulation/flip-initial-rotation-upside-down)
  (swap! state update :algorithm manipulation/flip-alg-string-upside-down))

(defn swap-layers-button [state]
  [common/button {:style {:white-space "pre-wrap"}
                  :on-click #(swap-layers state)}
   [:span
    [common/glyphicon {:glyph :refresh}]
    " Swap layers"]])

