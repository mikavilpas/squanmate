(ns squanmate.solving
  (:require [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.serialization :as serialization]
            [squanmate.alg.types :as types]
            [squanmate.rotation :as rotation]
            [squanmate.slicing :as slicing]))

(def conversions {[:top :front :left] "A"
                  [:top :left] "1"
                  [:top :left :back] "B"
                  [:top :back] "2"
                  [:top :back :right] "C"
                  [:top :right] "3"
                  [:top :right :front] "D"
                  [:top :front] "4"

                  [:bottom :front] "5"
                  [:bottom :front :right] "E"
                  [:bottom :right] "6"
                  [:bottom :right :back] "F"
                  [:bottom :back] "7"
                  [:bottom :back :left] "G"
                  [:bottom :left] "8"
                  [:bottom :left :front] "H"})

(defn- new-solver []
  ;; api:
  ;; new Worker("js/solver-worker.js").proxy()("solve")("start_state_encoded", function(err,result){[]});
  (js* "new Worker('js/solver-worker.js').proxy()('solve')"))

(defn- set-solution [result-alg
                     initial-rotation
                     result-atom]
  (either/branch (parser/parse result-alg)
                 (fn [error]
                   (println "Internal error: could not parse result algorithm: " result-alg
                            ", reason: " error))
                 (fn [alg-steps]
                   (let [result-steps (manipulation/prepend-initial-rotation
                                       initial-rotation
                                       alg-steps)]
                     (reset! result-atom
                             (serialization/alg-to-str result-steps))))))

(defn solve-state-string [starting-state-string & {:keys [initial-rotation result-atom]}]
  (let [solver (new-solver)]
    (solver starting-state-string
            (fn callback [err, result-alg]
              (when err
                (reset! result-atom (str "failed: " err)))
              (when result-alg
                (println "Initial rotation: " initial-rotation ", Solution: " result-alg)
                (set-solution result-alg initial-rotation result-atom))))
    result-atom))

(defn- convert-piece [p]
  (let [piece-id (filterv some?
                          [(-> p :colors :top)
                           (-> p :colors :a)
                           (-> p :colors :b)])]
    (get conversions
         piece-id
         (str "oops! piece " (pr-str p) " could not be converted!"))))

(defn convert-to-state-string [puzzle]
  (let [pieces (into (-> puzzle :top-layer :pieces)
                     (-> puzzle :bottom-layer :pieces))]
    (apply str
           (mapv convert-piece pieces))))

(defn- rotate-layer-to-slice-position [layer]
  (let [rotation-tries (rotation/random-layer-rotations layer)
        sliceable? (fn [[l _amount]]
                     (slicing/layer-sliceable? l))
        [layer-result amount] (first (filter sliceable? rotation-tries))]
    [layer-result amount]))

(defn- rotation-to-slice-position [puzzle]
  (if (and (slicing/top-sliceable? puzzle)
           (slicing/bottom-sliceable? puzzle))
    [(types/->Rotations 0 0) puzzle] ;; no need to rotate

    (let [[top top-rotation] (rotate-layer-to-slice-position (:top-layer puzzle))
          [bottom bottom-rotation] (rotate-layer-to-slice-position (:bottom-layer puzzle))

          new-puzzle (-> puzzle
                         (assoc :top-layer top)
                         (assoc :bottom-layer bottom))

          rotations (types/->Rotations top-rotation bottom-rotation)]
      [rotations new-puzzle])))

;; this is the intended public api
(defn solve
  ([puzzle]
   (solve puzzle (reagent/atom nil)))

  ([puzzle result-atom]
   (let [[rotation puzzle] (rotation-to-slice-position puzzle)]
     ;; This is a limitation of Jaap's solver: the puzzle must be at a
     ;; sliceable position when a solution is calculated. To work around
     ;; this, twist the puzzle with a random rotation so that it's sliceable,
     ;; and include that random rotation in the scramble.
     (solve-state-string (convert-to-state-string puzzle)
                         :initial-rotation rotation
                         :result-atom result-atom))))
