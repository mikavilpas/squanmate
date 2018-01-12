(ns squanmate.solving
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.alg.parser :as parser]
            [squanmate.alg.serialization :as serialization]
            [squanmate.alg.types :as types]
            [squanmate.alg.rotation :as rotation]
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

(defn solve-state-string [starting-state-string & {:keys [initial-rotation]}]
  (let [solver (new-solver)
        result-atom (reagent/atom nil)]
    (solver starting-state-string
            (fn callback [err, result-alg]
              (when err
                (reset! result-atom (str "failed: " err)))
              (when result-alg
                (println "puzzle: " starting-state-string
                         ", Initial rotation: " initial-rotation
                         ", Solution: " result-alg)
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

(defn convert-to-state-string [puzzle flip-middle?]
  (let [pieces (into (-> puzzle :top-layer :pieces)
                     (-> puzzle :bottom-layer :pieces))
        middle (if flip-middle?
                 "/"
                 "-")]
    (str (apply str
                (mapv convert-piece pieces))
         middle)))

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

(defn- should-flip-middle? [state]
  (let [mode (-> @state
                 :middle-layer-settings
                 :flip-mode)]
    (condp = mode
      :random (rand-nth [true false])
      :always true
      :never false
      (rand-nth [true false]))))

(defn solve
  ([puzzle]
   (solve puzzle false))
  ([puzzle flip-middle?]
   (let [[rotation puzzle] (rotation-to-slice-position puzzle)]
     ;; This is a limitation of Jaap's solver: the puzzle must be at a
     ;; sliceable position when a solution is calculated. To work around
     ;; this, twist the puzzle with a random rotation so that it's sliceable,
     ;; and include that random rotation in the scramble.
     (solve-state-string (convert-to-state-string puzzle flip-middle?)
                         :initial-rotation rotation))))

(defn solve-and-generate-scramble
  "Creates a scramble for the given puzzle. When generating it is ready, puts
  the scramble to :scramble-algorithm inside the given state"
  ([puzzle]
   (solve-and-generate-scramble puzzle (reagent/atom nil)))
  ([puzzle state]
   (let [solution-atom (solve puzzle
                              (should-flip-middle? state))]
     (add-watch solution-atom nil
                (fn [_key _ref _old-value solution-algorithm]
                  (let [steps (m/extract (parser/parse solution-algorithm))
                        reverse-steps (manipulation/reverse-steps steps)
                        scramble-string (serialization/alg-to-str reverse-steps)]
                    (swap! state assoc :scramble-algorithm scramble-string))))
     state)))
