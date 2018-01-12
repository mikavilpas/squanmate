(ns squanmate.scramblers.shape-scrambler.default-scrambler
  (:require [squanmate.puzzle :as p]
            [squanmate.rotation :as rotation]
            [squanmate.scramblers.shape-scrambler.scrambler :as scrambler]
            [squanmate.services.shape-combinations :as shape-combinations]
            [squanmate.shapes :as shapes]))

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

(defn- shape-str [shape-name]
  (p/pieces-str (get shapes/all-shapes shape-name)))

(defn apply-random-rotations [puzzle]
  (let [[new-top _] (-> puzzle :top-layer rotation/random-sliceable-rotations first)
        [new-bottom _] (-> puzzle :bottom-layer rotation/random-sliceable-rotations first)]
    (assoc puzzle
           :top-layer new-top
           :bottom-layer new-bottom)))

(defn new-default-shape-scrambler
  "Generates scrambles that are guaranteed to start with the layers in the desired
  shapes. This exists to make practicing cubeshape and cubeshape parity
  algorithms easier."
  ([]
   (new-default-shape-scrambler shape-combinations/possible-layers))
  ([possible-layers]
   (reify scrambler/ShapeScrambler
     (create-scramble [this]
       (let [[top-name bottom-name] (random-top-and-bottom-shape-names possible-layers)
             top (shape-str top-name)
             bottom (shape-str bottom-name)
             scrambled-puzzle (-> (p/puzzle-with-shapes top bottom)
                                  apply-random-rotations)]
         [[top-name bottom-name]
          scrambled-puzzle])))))
