(ns squanmate.services.cube-aligner
  (:require [cats.monad.either :as either]
            [squanmate.shapes :as shapes]
            [squanmate.puzzle :as p]
            [squanmate.alg.rotation :as rotation]
            [squanmate.alg.types :as types]))

(defn- oriented? [layer]
  (condp = (type layer)
    p/TopLayer (shapes/same-shape-and-orientation? layer
                                                   (:top-layer p/square-square))
    p/BottomLayer (shapes/same-shape-and-orientation? layer
                                                      (:bottom-layer p/square-square))
    (throw (new js/Error "unknown layer type " (type layer)))))

(defn- rotation-to-align-square-layer [layer]
  (if (oriented? layer)
    0

    (->> (rotation/possible-rotations layer [1, -1, 2, -2])
         (filter (fn [[puzzle amount]]
                   (oriented? puzzle)))
         (map (fn [[puzzle amount]]
                amount))
         first)))

(defn- rotations-to-aligned [puzzle]
  (let [top-amount (rotation-to-align-square-layer (:top-layer puzzle))
        bottom-amount (rotation-to-align-square-layer (:bottom-layer puzzle))]
    (types/Rotations. top-amount bottom-amount)))

(defn rotations-to-align-cube [puzzle]
  (let [layer-names (shapes/puzzle-layer-shape-names puzzle)]
    (if (not (= ["square" "square"] layer-names))
      (either/left (str "puzzle is not in cubesahpe. Instead it has these layers: " layer-names))
      (either/right (rotations-to-aligned puzzle)))))
