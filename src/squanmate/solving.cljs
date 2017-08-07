(ns squanmate.solving
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
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

(defn- combine-rotations [a b]
  (-> a
      (update :top-amount #(+ % (:top-amount b)))
      (update :bottom-amount #(+ % (:bottom-amount b)))))

(defn- prepend-initial-rotation [rotation alg-steps]
  (let [s (first alg-steps)]
    (cond
      (nil? rotation)
      alg-steps

      (= types/Rotations (type s))
      (let [new-first-step (combine-rotations s rotation)]
        (into [new-first-step] (rest alg-steps)))

      :else (into [rotation] alg-steps))))

(defn solve-state-string
  ([starting-state-string]
   (solve-state-string starting-state-string nil))

  ([starting-state-string initial-rotation]
   (let [result-atom (atom nil)
         solver (new-solver)]
     (solver starting-state-string
             (fn callback [err, result-alg]
               (when err
                 (reset! result-atom (str "failed: " err)))
               (when result-alg
                 (println "Initial rotation: " initial-rotation ", Solution: " result-alg)
                 (let [alg-steps (m/extract (parser/parse result-alg))
                       result-steps (prepend-initial-rotation initial-rotation alg-steps)]
                   (reset! result-atom
                           (serialization/alg-to-str result-steps))))))
     result-atom)))

(defn- convert-piece [p]
  (let [piece-id (filterv some?
                          [(-> p :colors :top)
                           (-> p :colors :a)
                           (-> p :colors :b)])]
    (get conversions
         piece-id
         (str "oops! piece " (pr-str p) " could not be converted!"))))

(defn- bottom-layer-pieces [puzzle]
  (let [turned-puzzle (execution/transformation-result puzzle "0,-6")]
    (either/branch
     turned-puzzle
     (fn [& _]
       (throw (new js/Error "the puzzle could not be converted to a state string for solving it.")))
     (fn [result]
       (-> result :puzzle :bottom-layer :pieces)))))

(defn convert-to-state-string [puzzle]
  (let [pieces (into (-> puzzle :top-layer :pieces)
                     (bottom-layer-pieces puzzle))]
    (apply str
           (mapv convert-piece pieces))))

(defn- rotate-layer-to-slice-position [layer]
  (let [rotation-amounts (shuffle (range -6 7))
        rotation-tries (map (fn [amount]
                              [(rotation/rotate-layer layer amount)
                               amount])
                            rotation-amounts)
        sliceable? (fn [[l _amount]]
                     (and (either/right? l)
                          (slicing/layer-sliceable? (m/extract l))))

        [layer-result amount] (first (filter sliceable? rotation-tries))]
    [(m/extract layer-result)
     amount]))

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
(defn solve [puzzle]
  (let [sliceable? (either/right? (slicing/slice puzzle))]
    (if sliceable?
      (solve-state-string (convert-to-state-string puzzle))
      (let [[rotation puzzle] (rotation-to-slice-position puzzle)]
        ;; This is a limitation of Jaap's solver: the puzzle must be at a
        ;; sliceable position when a solution is calculated. To work around
        ;; this, twist the puzzle with a random rotation so that it's sliceable,
        ;; and include that random rotation in the scramble.
        (solve-state-string (convert-to-state-string puzzle)
                     rotation)))))
