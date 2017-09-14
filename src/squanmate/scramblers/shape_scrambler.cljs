(ns squanmate.scramblers.shape-scrambler
  (:require [cats.core :as m]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.serialization :as serialization]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as rotation]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.shapes :as shapes]
            [squanmate.solving :as solving]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.layer-selector :as layer-selector]
            [squanmate.ui.color-chooser :as color-chooser]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn- random-top-and-bottom-shape-names [possible-layers]
  ;; possible-layers like square square are just represented as #{"square"},
  ;; which is only one shape name. Need to make sure this will work too.
  (let [layer-names (->> possible-layers
                         vec
                         rand-nth
                         cycle
                         (take 2))]
    (-> layer-names
        ;; mix the top and bottom layers together so they randomly change
        shuffle
        vec)))

(defn- apply-random-rotations [puzzle]
  (let [[new-top _] (-> puzzle :top-layer rotation/random-layer-rotations first)
        [new-bottom _] (-> puzzle :bottom-layer rotation/random-layer-rotations first)]
    (assoc puzzle
           :top-layer new-top
           :bottom-layer new-bottom)))

(defn scramble
  "A shape scramble is a scramble that is guaranteed to start with the layers in
  the desired shapes. This exists to make practicing cubeshape and cubeshape
  parity algorithms easier."
  ([]
   (scramble shape-combinations/possible-layers))
  ([possible-layers]
   (let [[top-name bottom-name] (random-top-and-bottom-shape-names possible-layers)
         top (shape-str top-name)
         bottom (shape-str bottom-name)]
     (-> (p/puzzle-with-shapes top bottom)
         apply-random-rotations))))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn- selected-shapes-counter [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)
        percentage (-> (* 100 (/ layer-count 90))
                       (.toFixed 2))]
    [:div (str layer-count " / 90 total shapes selected (" percentage " %).")]))

(defonce all-layers (->> shape-combinations/possible-layers
                         (map set)
                         set))

(defn- select-all-shapes [state]
  (swap! state assoc :selected-shapes all-layers))

(defn- select-no-shapes [state]
  (swap! state assoc :selected-shapes #{}))

(defn- all-shapes-selection-buttons [state]
  [:div
   [common/button {:on-click #(select-all-shapes state)} "Select all"]
   " "
   [common/button {:on-click #(select-no-shapes state)} "Select none"]])

(defn- shape-selection-settings [state]
  [:div
   [selected-shapes-counter state]
   [all-shapes-selection-buttons state]
   [:div.top30
    "Select available shapes for scrambles by filtering:"]
   [layer-selector/layer-selector state]])

(defn settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Settings"])
                  :event-key 1}
    [shape-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 2}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn new-scramble! [state]
  (let [new-scramble (scramble (:selected-shapes @state))
        solution-atom (solving/solve new-scramble)]
    (swap! state assoc :scramble-algorithm nil)
    (swap! state assoc :puzzle new-scramble)
    (add-watch solution-atom nil
               (fn [_key _ref _old-value solution-algorithm]
                 (let [steps (m/extract (parser/parse solution-algorithm))
                       reverse-steps (manipulation/reverse-steps steps)
                       scramble-string (serialization/alg-to-str reverse-steps)]
                   (swap! state assoc :scramble-algorithm scramble-string))))))

;; let this module own its state schema by having it defined inside this file
(defn new-state []
  ;; Allowed shapes are stored as sets of their layers. This makes adding and
  ;; removing them very easy in code.
  (let [state (reagent/atom
               {:puzzle nil
                :selected-shapes #{(set ["square" "square"])}
                :scramble-algorithm nil
                :draw-settings (deref (color-chooser/default-color-chooser-state))})]
    (new-scramble! state)
    state))

(defn scramble-component [state]
  (let [draw-settings (assoc (:draw-settings @state)
                        :size 180)]
    [:div
     [:div.center
      [newmonochrome/monochrome-puzzle (:puzzle @state) draw-settings]]
     [:div.center
      [scramble-preview (:scramble-algorithm @state)]]
     [settings state]]))
