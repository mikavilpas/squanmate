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
            [reagent.core :as reagent]))

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn- random-top-and-bottom-shape-names []
  (shuffle (rand-nth shape-combinations/possible-layers)))

(defn scramble
  "A shape scramble is a scramble that is guaranteed to start with the layers in
  the desired shapes. This exists to make practicing cubeshape and cubeshape
  parity algorithms easier."
  []
  (let [[top-name bottom-name] (random-top-and-bottom-shape-names)
        top (shape-str top-name)
        bottom (shape-str bottom-name)]
    (p/puzzle-with-shapes top bottom)))

(defn- scramble-preview [s]
  [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]])

(defn- checkbox-handler [id state]
  (swap! state update-in [:selected-shapes id] not)
  (println "state is now " (-> @state :selected-shapes pr-str)))

(defn settings [state]
  [common/accordion
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Settings"])
                  :event-key 1}
    (into [:div] (for [[a b] shape-combinations/possible-layers]
                   (let [id (str a " " b)]
                     [common/checkbox {:id id
                                       :inline true
                                       :checked (get-in @state [:selected-shapes id])
                                       :on-change #(checkbox-handler id state)}
                      (str a " " b)])))]])

(defn new-scramble! [state]
  (swap! state assoc :puzzle (scramble)))

;; let this module own its state schema
(defn new-state []
  (reagent/atom
   {:puzzle (scramble)
    :selected-shapes {"square square" true}}))

(defn scramble-component [state]
  (let [solution-atom (solving/solve (:puzzle @state))]
    (fn [state]
      (let [solution-alg @solution-atom
            steps (m/extract (parser/parse solution-alg))
            reverse-steps (manipulation/reverse-steps steps)
            scramble-string (serialization/alg-to-str reverse-steps)]
        [:div
         [:div.center
          [newmonochrome/monochrome-puzzle (:puzzle @state)
           {:monochrome? false
            :size 180}]]
         [:div.center
          [scramble-preview scramble-string]]
         [settings state]]))))
