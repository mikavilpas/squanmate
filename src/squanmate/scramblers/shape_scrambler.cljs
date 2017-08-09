(ns squanmate.scramblers.shape-scrambler
  (:require [squanmate.puzzle :as p]
            [squanmate.shapes :as shapes]
            [squanmate.solving :as solving]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.serialization :as serialization]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [cats.core :as m]
            [squanmate.ui.common :as common]
            [squanmate.shape-combinations :as shape-combinations]
            [reagent.core :as reagent]
            [squanmate.rotation :as rotation]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn- random-top-and-bottom-shape-names [possible-layers]
  (-> possible-layers
      ;; this is called with a set, which is unusable with rand-nth. Need to
      ;; convert it.
      vec
      rand-nth
      ;; mix the top and bottom layers together so they randomly change
      shuffle))

(defn- apply-random-rotations [puzzle]
  (let [[new-top _] (-> puzzle :top-layer rotation/random-layer-rotations first)
        [new-bottom _] (-> puzzle :bottom-layer rotation/random-layer-rotations first)]
    (assoc puzzle
           :top-layer (m/extract new-top)
           :bottom-layer (m/extract new-bottom))))

(defn scramble
  "A shape scramble is a scramble that is guaranteed to start with the layers in
  the desired shapes. This exists to make practicing cubeshape and cubeshape
  parity algorithms easier."
  ([]
   (scramble shape-combinations/possible-layers))
  ([possible-layers]
   (let [result (random-top-and-bottom-shape-names possible-layers)
         [top-name bottom-name] (random-top-and-bottom-shape-names possible-layers)
         top (shape-str top-name)
         bottom (shape-str bottom-name)]
     (-> (p/puzzle-with-shapes top bottom)
         apply-random-rotations))))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn- checkbox-handler [shape-names state]
  (if (contains? (:selected-shapes @state) shape-names)
    (swap! state update-in [:selected-shapes] disj shape-names)
    (swap! state update-in [:selected-shapes] conj shape-names)) )

(defn settings [state]
  [common/accordion
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Settings"])
                  :event-key 1}
    (into [:div] (for [[a b] shape-combinations/possible-layers]
                   (let [id (str a " " b)]
                     [common/checkbox {:id id
                                       :inline true
                                       :checked (contains? (:selected-shapes @state) [a b])
                                       :on-change #(checkbox-handler [a b] state)}
                      (str a " " b)])))]])

(defn new-scramble! [state]
  (let [new-scramble (scramble (:selected-shapes @state))
        solution-atom (solving/solve new-scramble)]
    (swap! state assoc :puzzle new-scramble)
    (add-watch solution-atom nil
               (fn [_key _ref _old-value solution-algorithm]
                 (let [steps (m/extract (parser/parse solution-algorithm))
                       reverse-steps (manipulation/reverse-steps steps)
                       scramble-string (serialization/alg-to-str reverse-steps)]
                   (swap! state assoc :scramble-algorithm scramble-string))))))

;; let this module own its state schema
(defn new-state []
  (let [state (reagent/atom
               {:puzzle nil
                :selected-shapes #{["square" "square"]}
                :scramble-algorithm nil})]
    (new-scramble! state)
    state))

(defn scramble-component [state]
  [:div
   [:div.center
    [newmonochrome/monochrome-puzzle (:puzzle @state) {:monochrome? false
                                                       :size 180}]]
   [:div.center
    [scramble-preview (:scramble-algorithm @state)]]
   [settings state]])
