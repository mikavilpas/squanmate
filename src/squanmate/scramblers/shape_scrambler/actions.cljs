(ns squanmate.scramblers.shape-scrambler.actions
  (:require [cats.core :as m]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.serialization :as serialization]
            [squanmate.puzzle :as p]
            [squanmate.rotation :as rotation]
            [squanmate.services.google-analytics :as ga]
            [squanmate.shape-combinations :as shape-combinations]
            [squanmate.shapes :as shapes]
            [squanmate.solving :as solving]))

(defonce all-layers (->> shape-combinations/possible-layers
                         (map set)
                         set))

(defn selected-shapes-count [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)
        percentage (-> (* 100 (/ layer-count 90))
                       (.toFixed 2))]
    [layer-count percentage]))

(defn selected-shapes-counter [state]
  ;; there are 90 total shape combinations
  (let [layer-count (-> @state :selected-shapes count)
        percentage (-> (* 100 (/ layer-count 90))
                       (.toFixed 2))]
    [:div (str layer-count " / 90 total shapes selected (" percentage " %).")]))

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
         bottom (shape-str bottom-name)
         scrambled-puzzle (-> (p/puzzle-with-shapes top bottom)
                              apply-random-rotations)]
     [[top-name bottom-name]
      scrambled-puzzle])))


(defn select-all-shapes [state]
  (swap! state assoc :selected-shapes all-layers))

(defn select-no-shapes [state]
  (swap! state assoc :selected-shapes #{}))

(defn new-scramble!
  ([state]
   (new-scramble! state (:selected-shapes @state)))

  ([state selected-shapes]
   (let [[chosen-layers new-scramble] (scramble selected-shapes)
         solution-atom (solving/solve new-scramble)]
     (swap! state assoc
            :scramble-algorithm nil
            :puzzle new-scramble
            :chosen-shapes (into #{} chosen-layers))
     ;; todo this is technically a part of solving. it shouldn't belong here.
     (add-watch solution-atom nil
                (fn [_key _ref _old-value solution-algorithm]
                  (let [steps (m/extract (parser/parse solution-algorithm))
                        reverse-steps (manipulation/reverse-steps steps)
                        scramble-string (serialization/alg-to-str reverse-steps)]
                    (swap! state assoc :scramble-algorithm scramble-string)))))))


(defn set-new-scramble [& args]
  (apply new-scramble! args)
  (ga/send-page-view :trainer/new-scramble))
