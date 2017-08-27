(ns squanmate.alg.serialization
  (:require [clojure.string :as str]
            [squanmate.alg.prettification :as prettification]
            [squanmate.alg.types :as types]
            [squanmate.rotation :as rotation]
            [squanmate.shapes :as shapes]))

(defn alg-to-str [steps]
  (str/join ""
            (for [s steps]
              (do
                (condp = (type s)
                  ;; carefully placing spaces only outside of (a,-b)/ groups
                  ;; makes the scramble to be rendered with very nice text
                  ;; wrapping in the browser :)
                  types/Slice "/ "
                  types/Rotations (str "(" (prettification/prettify-value
                                            (:top-amount s))
                                       ","
                                       (prettification/prettify-value
                                        (:bottom-amount s))
                                       ")"))))))

(defn rotation-specification
  "Returns the key (programmatical name) of the layer and its initial rotation
  (from the default rotation) as a vector."
  [layer]
  (let [layer-key (shapes/layer-shape-name-key layer)
        rotations (rotation/possible-rotations (get shapes/all-shapes layer-key))
        same-shape? (fn [[l rotation-amount]]
                      (shapes/same-shape-and-orientation? layer l))
        all-same-shapes (filter same-shape? rotations)
        [_ rotation-amount] (first (filter same-shape? rotations))]
    [layer-key rotation-amount]))

(defn puzzle-specification [p]
  (let [[top-name top-rotation] (-> p :top-layer rotation-specification)
        [bottom-name bottom-rotation] (-> p :top-layer rotation-specification)
        initial-rotation (types/->Rotations top-rotation
                                            bottom-rotation)]
    {:top-name top-name
     :bottom-name bottom-name
     :initial-rotation initial-rotation}))
